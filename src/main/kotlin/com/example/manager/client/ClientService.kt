package com.example.manager.client

import com.example.manager.exceptions.ApiRequestException
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



    //get client by id
    fun getClient(id : Long) : Client{
        val result = clientRepository.findById(id)
        if(result.isEmpty)
            throw  ApiRequestException("Client with this ID doesn't exist!")
        return result.get()
    }


    //get all registered clients
    fun getClients() : List<Client>{
        val result = clientRepository.findAll()
        if(result.isEmpty())
            throw ApiRequestException("No one client registered")
        return result
    }

    /*
    register client - check if client with same username doesn't exist
    save client with encrypted private key and hashed password
    return client stay-login key
    */
    fun registerClient(client: Client) : ClientKeyIdData {
        //check if username isn't already taken
        if(clientRepository.findByName(client.username) != null)
            throw ApiRequestException("Client with this name already exists!")

        //generate asymmetrical key pair to encrypt passwords
        val keyPair = AsymmetricalCryptoUtils.generateCryptoKeys(2048)
        //generate secret key from password to encrypt private key
        val secretKey = SymmetricalCryptoUtils.getKeyFromPassword(client.password)

        /*
            encrypt private key by secret key
            hash password
            save client to database
         */
        client.privateKey = SymmetricalCryptoUtils.encryptMessageAsBase64(secretKey, keyPair.private.encoded)
        client.password = Base64.getEncoder()
            .encodeToString(Hashing.sha256().hashString(client.password, StandardCharsets.UTF_8).asBytes())
        clientRepository.save(client)

        /*
            create instance for stay-login with empty password and unencrypted private key
            generate new stay-login key and send it to user
         */
        client.password = ""
        client.privateKey = Base64.getEncoder().encodeToString(keyPair.private.encoded)
        var newLoginKey = ""
        do {
            newLoginKey = Utils.getRandomString(30)
        }while (logged[newLoginKey] != null)
        logged[newLoginKey] = client

        return  ClientKeyIdData(client.id, newLoginKey)
    }

    /*
    set up pin for stay-login on device
    return client private key encrypted by pin

     */
    fun registerSetUpPin(clientKeyPinData: ClientKeyPinData) : String{
        //check if stay-login key is valid
        var client: Client = logged[clientKeyPinData.key] ?: throw ApiRequestException("Api Key is Invalid!")

        //check if pin isn't already set
        if(client.password != "")
            throw ApiRequestException("Pin is already set!")

        //generate secrete key from pin
        val secretKey = SymmetricalCryptoUtils.getKeyFromPassword(clientKeyPinData.pinCode)

        //hash pin
        client.password = Base64.getEncoder()
            .encodeToString(Hashing.sha256().hashString(clientKeyPinData.pinCode, StandardCharsets.UTF_8).asBytes())
        //encrypt private key by secret key
        client.privateKey = SymmetricalCryptoUtils.encryptMessageAsBase64(secretKey, Base64.getDecoder().decode(client.privateKey))

        return client.privateKey
    }


    /*
    login client on device
    return client stay-login key
     */
    fun loginClient(client: Client): ClientKeyIdData {
        //checks if user exists
        val clientInDB = clientRepository.findByNameAndPassword(
            client.username, Base64.getEncoder()
                .encodeToString(Hashing.sha256().hashString(client.password, StandardCharsets.UTF_8).asBytes())
        ) ?: throw ApiRequestException("Client doesn't exist!")

        /*
            create stay login instance on server with
            decrypted private key and empty password
            generate stay-login key and send it to client
         */

        client.let {
            it.id = clientInDB.id
            it.privateKey =
                Base64.getEncoder().encodeToString(SymmetricalCryptoUtils.decryptMessage(client.password, clientInDB.privateKey))
            it.password = ""
        }

        var newLoginKey = ""
        do {
            newLoginKey = Utils.getRandomString(30)
        } while (logged[newLoginKey] != null)
        logged[newLoginKey] = client
        return ClientKeyIdData(client.id, newLoginKey)
    }

    /*
    login with stay-login pin
    return client private key encrypted by pin
     */
    fun loginWithPin(clientKeyPinData: ClientKeyPinData): String {
        //check if stay-login key is valid
        var client: Client = logged[clientKeyPinData.key] ?: throw ApiRequestException("Stay-login key is invalid!")


        //if everything in order return client encrypted private key
        if (client.password == Base64.getEncoder().encodeToString(Hashing.sha256().hashString(clientKeyPinData.pinCode, StandardCharsets.UTF_8).asBytes()))
            return client.privateKey
        //if combination of stay-login key and pin isn't valid
        throw ApiRequestException("Pin is invalid!")
    }



}