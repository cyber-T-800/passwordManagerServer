package com.example.manager.password

import com.example.manager.client.Client
import com.example.manager.client.ClientKeyPinData
import com.example.manager.client.ClientService
import com.example.manager.utils.AsymmetricalCryptoUtils
import com.example.manager.utils.SymmetricalCryptoUtils
import com.google.common.hash.Hashing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.PublicKey
import java.util.*
import javax.crypto.SecretKey
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities

@Service
class PasswordService {
    @Autowired
    lateinit var passwordRepository:  PasswordRepository

    @Autowired
    lateinit var clientService: ClientService

    /*
        endpoint for save password
        return password ID
        return 0 if stay-logged key or pin is invalid
     */
    fun savePassword(passwordRequestData: PasswordRequestData) : Long{
        val requestedClient : Client = clientService.logged[passwordRequestData.clientKeyPinData.key] ?: return 0
        //check if pin code is valid
        if(requestedClient.password != Base64.getEncoder().encodeToString(Hashing.sha256().hashString(passwordRequestData.clientKeyPinData.pinCode, StandardCharsets.UTF_8).asBytes()))
            return 0
        val publicKey : PublicKey
        passwordRequestData.clientKeyPinData.let {
            val privateKey = AsymmetricalCryptoUtils.privateKeyFromBytes(SymmetricalCryptoUtils.decryptMessage(it.pinCode, requestedClient.privateKey))
            publicKey = AsymmetricalCryptoUtils.publicKeyFromPrivate(privateKey)
        }

        //encrypt by client public key
        passwordRequestData.password.let {
            it.clientId = requestedClient.id
            it.encryptedPassword = AsymmetricalCryptoUtils.encryptMessageAsBase64(publicKey, it.encryptedPassword.toByteArray(StandardCharsets.UTF_8))
            passwordRepository.save(it)
            return it.id
        }
    }


    /*
        get passwords by combination of client API key and pin code
        return null combination is invalid
     */
    fun getClientsPasswords(clientKeyPinData: ClientKeyPinData) : Collection<Password>?{
        val requestedClient : Client = clientService.logged[clientKeyPinData.key] ?: return null
        //check if pin code is valid
        if(requestedClient.password != Base64.getEncoder().encodeToString(Hashing.sha256().hashString(clientKeyPinData.pinCode, StandardCharsets.UTF_8).asBytes()))
            return null
        return passwordRepository.findByClientId(requestedClient.id)
    }


}