package com.example.manager.client

import com.example.manager.utils.AsymmetricalCryptoUtils
import com.example.manager.utils.SymmetricalCryptoUtils
import com.google.common.hash.Hashing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.stereotype.Service
import java.io.Serial
import java.nio.charset.StandardCharsets
import java.util.*

@Service
class ClientService {
    @Autowired
    lateinit var clientRepository : ClientRepository


    fun getClient(id : Long) : Client{
        return clientRepository.findById(id).get()
    }

    fun getClients() : List<Client>{
        return clientRepository.findAll()
    }

    fun registerClient(client: Client): Boolean {
        if(clientRepository.findByName(client.username) == null){
            //Generate key pair to encrypt saved passwords
            val keyPair = AsymmetricalCryptoUtils.generateCryptoKeys(2048)

            //Generate secret key from password to encrypt private key
            val secretKey = SymmetricalCryptoUtils.getKeyFromPassword(client.password)

            //Save encrypted private key
            client.privateKey = SymmetricalCryptoUtils.encryptMessageAsBase64(secretKey, keyPair.private.encoded)

            //Save hashed password
            client.password = Base64.getEncoder().encodeToString(Hashing.sha256().hashString(client.password, StandardCharsets.UTF_8).asBytes())

            clientRepository.save(client)
            return true
        }else
            return false

    }


}