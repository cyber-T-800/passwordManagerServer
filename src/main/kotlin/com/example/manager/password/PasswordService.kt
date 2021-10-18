package com.example.manager.password

import com.example.manager.client.Client
import com.example.manager.client.ClientKeyPinData
import com.example.manager.client.ClientService
import com.example.manager.exceptions.ApiRequestException
import com.example.manager.utils.AsymmetricalCryptoUtils
import com.example.manager.utils.SymmetricalCryptoUtils
import com.google.common.hash.Hashing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
     */
    fun savePassword(passwordRequestData: PasswordRequestData) : Long{
        val requestedClient : Client = clientService.logged[passwordRequestData.clientKeyPinData.key] ?: throw ApiRequestException("Stay-login key is invalid!")
        //check if pin code is valid
        if(requestedClient.password != Base64.getEncoder().encodeToString(Hashing.sha256().hashString(passwordRequestData.clientKeyPinData.pinCode, StandardCharsets.UTF_8).asBytes()))
            throw ApiRequestException("Pin is invalid!")
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
     */
    fun getClientsPasswords(clientKeyPinData: ClientKeyPinData) : Collection<Password>?{
        val requestedClient : Client = clientService.logged[clientKeyPinData.key] ?: throw ApiRequestException("Stay-login key is invalid!")
        //check if pin code is valid
        if(requestedClient.password != Base64.getEncoder().encodeToString(Hashing.sha256().hashString(clientKeyPinData.pinCode, StandardCharsets.UTF_8).asBytes()))
            throw ApiRequestException("Pin code is invalid!")
        return passwordRepository.findByClientId(requestedClient.id)
    }
    /*
        delete password from database

     */
    fun deletePassword(passwordRequestData: PasswordRequestData) {
        //check if client instance is valid
        val requestedClient : Client = clientService.logged[passwordRequestData.clientKeyPinData.key] ?: throw ApiRequestException("Stay-login key is invalid!")
        //check if pin code is valid
        if(requestedClient.password != Base64.getEncoder().encodeToString(Hashing.sha256().hashString(passwordRequestData.clientKeyPinData.pinCode, StandardCharsets.UTF_8).asBytes()))
            throw ApiRequestException("Pin code is invalid!")
        /*
            checks if requested is same as password owner id
            and if requested password owner id is same as password owner id in database
         */
        if(requestedClient.id == passwordRequestData.password.clientId && passwordRequestData.password.clientId == passwordRepository.getById(passwordRequestData.password.id).clientId){
            passwordRepository.deleteById(passwordRequestData.password.id)
        }
        else{ throw ApiRequestException("Client data are invalid!") }
    }
    /*
        edit password in database
    */
    @Transactional
    fun editPassword(passwordRequestData: PasswordRequestData) {
        val requestedClient : Client = clientService.logged[passwordRequestData.clientKeyPinData.key] ?: throw ApiRequestException("Stay-login key is invalid!")
        //check if pin code is valid
        if(requestedClient.password != Base64.getEncoder().encodeToString(Hashing.sha256().hashString(passwordRequestData.clientKeyPinData.pinCode, StandardCharsets.UTF_8).asBytes()))
            throw ApiRequestException("Pin code is invalid!")
        /*
            checks if requested is same as password owner id
            and if requested password owner id is same as password owner id in database
         */
        if(requestedClient.id == passwordRequestData.password.clientId && passwordRequestData.password.clientId == passwordRepository.getById(passwordRequestData.password.id).clientId){
            val publicKey : PublicKey
            passwordRequestData.clientKeyPinData.let {
                val privateKey = AsymmetricalCryptoUtils.privateKeyFromBytes(SymmetricalCryptoUtils.decryptMessage(it.pinCode, requestedClient.privateKey))
                publicKey = AsymmetricalCryptoUtils.publicKeyFromPrivate(privateKey)
            }

            passwordRequestData.password.let {
                it.encryptedPassword = AsymmetricalCryptoUtils.encryptMessageAsBase64(publicKey, it.encryptedPassword.toByteArray(StandardCharsets.UTF_8))
                passwordRepository.update(it.id, it.website, it.username, it.encryptedPassword)
            }
        }else{
            throw ApiRequestException("Client data are invalid!")
        }
    }


}