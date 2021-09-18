package com.example.manager.client

import com.example.manager.utils.AsymmetricalCryptoUtils
import com.example.manager.utils.SymmetricalCryptoUtils
import com.example.manager.utils.Utils
import com.fasterxml.jackson.databind.ser.Serializers
import com.google.common.hash.Hashing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.HashMap

@Service
class ClientService {
    @Autowired
    lateinit var clientRepository : ClientRepository
    var logined : HashMap<String, Client> = hashMapOf()

    fun getClient(id : Long) : Client?{
        return clientRepository.findById(id).get()
    }

    fun getClients() : List<Client>{
        return clientRepository.findAll()
    }


    //register client - check if client with same username doesn't exist
    //save client with encrypted private key and hashed password
    fun registerClient(client: Client) : String {
        if(clientRepository.findByName(client.username) != null)
            return "0"

        val keyPair = AsymmetricalCryptoUtils.generateCryptoKeys(2048)
        val secretKey = SymmetricalCryptoUtils.getKeyFromPassword(client.password)

        client.privateKey = SymmetricalCryptoUtils.encryptMessageAsBase64(secretKey, keyPair.private.encoded)
        client.password = Base64.getEncoder()
            .encodeToString(Hashing.sha256().hashString(client.password, StandardCharsets.UTF_8).asBytes())

        clientRepository.save(client)


        client.password = ""
        client.privateKey = Base64.getEncoder().encodeToString(keyPair.private.encoded)

        val newLoginKey = Utils.getRandomString(30)

        logined.put(newLoginKey, client)

        return  newLoginKey
    }

    fun registerSetUpPin(clientPinSetUp: ClientPinSetUp) : String{
        var client = logined.get(clientPinSetUp.key)!!

        if(client.password != "")
            return "0"

        val secretKey = SymmetricalCryptoUtils.getKeyFromPassword(clientPinSetUp.pinCode)


        client.password = Base64.getEncoder()
            .encodeToString(Hashing.sha256().hashString(clientPinSetUp.pinCode, StandardCharsets.UTF_8).asBytes())
        client.privateKey = SymmetricalCryptoUtils.encryptMessageAsBase64(secretKey, Base64.getDecoder().decode(client.privateKey))

        return client.privateKey
    }


}