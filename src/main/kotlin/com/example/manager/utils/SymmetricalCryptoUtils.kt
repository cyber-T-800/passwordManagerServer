package com.example.manager.utils

import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object SymmetricalCryptoUtils {

    var salt = "fdioanindvninioaneeiofeoovn"

    fun getKeyFromPassword(password: String) : SecretKey {
        val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val keySpec = PBEKeySpec(password.toCharArray(), salt.toByteArray(), 65536, 256)
        return SecretKeySpec(secretKeyFactory.generateSecret(keySpec).encoded, "AES")
    }

    fun initializeEncryptCipher(key: SecretKey) : Cipher {
        val result = Cipher.getInstance("AES")
        result.init(Cipher.ENCRYPT_MODE, key)
        return result
    }

    fun initializeDecryptCipher(key: SecretKey) : Cipher{
        var result = Cipher.getInstance("AES")
        result.init(Cipher.DECRYPT_MODE, key)
        return result
    }

    fun encryptMessage(cipher: Cipher, message : ByteArray) : ByteArray{
        return cipher.doFinal(message)
    }

    fun encryptMessage(key : SecretKey, message: ByteArray) : ByteArray{
        return encryptMessage(initializeEncryptCipher(key), message)
    }

    fun encryptMessage(password: String, message: ByteArray) : ByteArray{
        return encryptMessage(getKeyFromPassword(password), message)
    }


    fun encryptMessage(cipher: Cipher, message : String) : ByteArray{
        return encryptMessage(cipher, message.toByteArray())
    }

    fun encryptMessage(key : SecretKey, message: String) : ByteArray{
        return encryptMessage(initializeEncryptCipher(key), message)
    }

    fun encryptMessage(password: String, message: String) : ByteArray{
        return encryptMessage(getKeyFromPassword(password), message)
    }

    fun encryptMessageAsBase64(cipher: Cipher, message : ByteArray) : String{
        return Base64.getEncoder().encodeToString(encryptMessage(cipher, message))
    }

    fun encryptMessageAsBase64(key : SecretKey, message: ByteArray) : String{
        return encryptMessageAsBase64(initializeEncryptCipher(key), message)
    }

    fun encryptMessageAsBase64(password: String, message: ByteArray) : String{
        return encryptMessageAsBase64(getKeyFromPassword(password), message)
    }

    fun encryptMessageAsBase64(cipher: Cipher, message : String) : String{
        return Base64.getEncoder().encodeToString(encryptMessage(cipher, message))
    }

    fun encryptMessageAsBase64(key : SecretKey, message: String) : String{
        return encryptMessageAsBase64(initializeEncryptCipher(key), message)
    }

    fun encryptMessageAsBase64(password: String, message: String) : String{
        return encryptMessageAsBase64(getKeyFromPassword(password), message)
    }



    fun decryptMessage(cipher: Cipher, encryptedMessage : ByteArray) : ByteArray{
        return cipher.doFinal(encryptedMessage)
    }
    fun decryptMessage(key: SecretKey, encryptedMessage: ByteArray) : ByteArray{
        return decryptMessage(initializeDecryptCipher(key), encryptedMessage)
    }
    fun decryptMessage(password: String, encryptedMessage: ByteArray) : ByteArray{
        return decryptMessage(getKeyFromPassword(password), encryptedMessage)
    }

    fun decryptMessage(cipher: Cipher, encryptedMessageAsBase64: String) : ByteArray{
        return decryptMessage(cipher, Base64.getDecoder().decode(encryptedMessageAsBase64))
    }

    fun decryptMessage(key: SecretKey, encryptedMessageAsBase64: String) : ByteArray{
        return decryptMessage(initializeDecryptCipher(key), encryptedMessageAsBase64)
    }
    fun decryptMessage(password: String, encryptedMessageAsBase64: String) : ByteArray{
        return decryptMessage(getKeyFromPassword(password), encryptedMessageAsBase64)
    }


    fun decryptMessageAsString(cipher: Cipher, encryptedMessage : ByteArray) : String{
        return String(cipher.doFinal(encryptedMessage))
    }
    fun decryptMessageAsString(key: SecretKey, encryptedMessage: ByteArray) : String{
        return decryptMessageAsString(initializeDecryptCipher(key), encryptedMessage)
    }
    fun decryptMessageAsString(password: String, encryptedMessage: ByteArray) : String{
        return decryptMessageAsString(getKeyFromPassword(password), encryptedMessage)
    }

    fun decryptMessageAsString(cipher: Cipher, encryptedMessageAsBase64: String) : String{
        return decryptMessageAsString(cipher, Base64.getDecoder().decode(encryptedMessageAsBase64))
    }

    fun decryptMessageAsString(key: SecretKey, encryptedMessageAsBase64: String) : String{
        return decryptMessageAsString(initializeDecryptCipher(key), encryptedMessageAsBase64)
    }
    fun decryptMessageAsString(password: String, encryptedMessageAsBase64: String) : String{
        return decryptMessageAsString(getKeyFromPassword(password), encryptedMessageAsBase64)
    }



}