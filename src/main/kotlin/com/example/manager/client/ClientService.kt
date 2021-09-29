package com.example.manager.client

import com.example.manager.utils.AsymmetricalCryptoUtils
import com.example.manager.utils.SymmetricalCryptoUtils
import com.example.manager.utils.Utils
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
    var logged : HashMap<String, Client> = hashMapOf()


    /*
    get client by id
     */
    fun getClient(id : Long) : Client?{
        return clientRepository.findById(id).get()
    }


    /*
    get all registered clients
     */
    fun getClients() : List<Client>{
        return clientRepository.findAll()
    }

    /*
    register client - check if client with same username doesn't exist
    save client with encrypted private key and hashed password
    return client stay-login key
    */
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

        var newLoginKey = ""
        do {
            newLoginKey = Utils.getRandomString(30)
        }while (logged.get(newLoginKey) != null)

        logged.put(newLoginKey, client)

        return  newLoginKey
    }


    /*
    set up pin for stay-login on device
    return client private key encrypted by pin
     */
    fun registerSetUpPin(clientPinSetUp: ClientPinSetUp) : String{
        var client: Client = logged.get(clientPinSetUp.key) ?: return "1"

        if(client.password != "")
            return "0"

        val secretKey = SymmetricalCryptoUtils.getKeyFromPassword(clientPinSetUp.pinCode)


        client.password = Base64.getEncoder()
            .encodeToString(Hashing.sha256().hashString(clientPinSetUp.pinCode, StandardCharsets.UTF_8).asBytes())
        client.privateKey = SymmetricalCryptoUtils.encryptMessageAsBase64(secretKey, Base64.getDecoder().decode(client.privateKey))

        return client.privateKey
    }


    /*
    login client on device
    return client stay-login key
     */
    fun loginClient(client: Client): String {
        val clientInDB = clientRepository.findByNameAndPassword(client.username, Base64.getEncoder()
            .encodeToString(Hashing.sha256().hashString(client.password, StandardCharsets.UTF_8).asBytes()))

        if(clientInDB != null){
            val secretKey = SymmetricalCryptoUtils.getKeyFromPassword(client.password)
            client.privateKey = Base64.getEncoder().encodeToString(SymmetricalCryptoUtils.decryptMessage(secretKey, clientInDB.privateKey))
            client.password = ""
            var newLoginKey = ""
            do {
                newLoginKey = Utils.getRandomString(30)
            }while (logged.get(newLoginKey) != null)

            logged.put(newLoginKey, client)
            return  newLoginKey
        }

        return "0"
    }

    /*
    login with stay-login pin
    return client private key encrypted by pin
     */
    fun loginWithPin(clientPinSetUp: ClientPinSetUp): String {
        var client: Client = logged.get(clientPinSetUp.key) ?: return "1"

        if (client.password == Base64.getEncoder().encodeToString(Hashing.sha256().hashString(clientPinSetUp.pinCode, StandardCharsets.UTF_8).asBytes()))
            return client.privateKey


        return "0"
    }



}