package com.example.manager.utils

import java.security.*
import java.security.interfaces.RSAPrivateCrtKey
import java.security.spec.EncodedKeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object AsymmetricalCryptoUtils{

    fun generateCryptoKeys(size : Int) : KeyPair{
        val result : KeyPair
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(size)
        result = generator.genKeyPair()

        return result
    }
    fun publicKeyFromPrivate(privateKey: PrivateKey) : PublicKey{
        val result : PublicKey
        val privateCtrKey = privateKey as RSAPrivateCrtKey
        val publicKeySpecs = RSAPublicKeySpec(privateCtrKey.modulus, privateCtrKey.publicExponent)
        val keyFactory = KeyFactory.getInstance("RSA")
        result = keyFactory.generatePublic(publicKeySpecs)

        return result
    }

    fun privateKeyToBytes(privateKey: PrivateKey) : ByteArray{
        return privateKey.encoded
    }
    fun privateKeyFromBytes(byteArray: ByteArray) : PrivateKey{
        val result : PrivateKey

        val keySpec : EncodedKeySpec = PKCS8EncodedKeySpec(byteArray)
        val keyFactory = KeyFactory.getInstance("RSA")
        result = keyFactory.generatePrivate(keySpec)

        return result
    }
    fun publicKeyToBytes(publicKey: PublicKey) : ByteArray{
        return publicKey.encoded
    }
    fun publicKeyFromBytes(byteArray: ByteArray) : PublicKey{
        val result : PublicKey

        val keySpec : EncodedKeySpec = X509EncodedKeySpec(byteArray)
        val keyFactory = KeyFactory.getInstance("RSA")
        result = keyFactory.generatePublic(keySpec)

        return result
    }

    fun initializeEncryptCipher(privateKey: PrivateKey) : Cipher{
        val result : Cipher = Cipher.getInstance("RSA")
        result.init(Cipher.ENCRYPT_MODE, privateKey)
        return result
    }
    fun initializeEncryptCipher(publicKey: PublicKey) : Cipher{
        val result : Cipher = Cipher.getInstance("RSA")
        result.init(Cipher.ENCRYPT_MODE, publicKey)
        return result
    }
    fun initializeDecryptCipher(privateKey: PrivateKey) : Cipher{
        val result : Cipher = Cipher.getInstance("RSA")
        result.init(Cipher.DECRYPT_MODE, privateKey)
        return result
    }
    fun initializeDecryptCipher(publicKey: PublicKey) : Cipher{
        val result : Cipher = Cipher.getInstance("RSA")
        result.init(Cipher.DECRYPT_MODE, publicKey)
        return result
    }

    fun encryptMessage(cipher : Cipher, message : ByteArray) : ByteArray{
        return cipher.doFinal(message)
    }
    fun encryptMessage(publicKey: PublicKey, message: ByteArray) : ByteArray{
        return encryptMessage(initializeEncryptCipher(publicKey), message)
    }
    fun encryptMessage(privateKey: PrivateKey, message: ByteArray) : ByteArray{
        return encryptMessage(initializeEncryptCipher(privateKey), message)
    }
    fun decryptMessage(cipher : Cipher, message : ByteArray) : ByteArray{
        return cipher.doFinal(message)
    }
    fun decryptMessage(publicKey: PublicKey, message : ByteArray) : ByteArray{
        return decryptMessage(initializeDecryptCipher(publicKey), message)
    }
    fun decryptMessage(privateKey: PrivateKey, message : ByteArray) : ByteArray{
        return decryptMessage(initializeDecryptCipher(privateKey), message)
    }
}

