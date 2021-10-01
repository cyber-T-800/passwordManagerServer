package com.example.manager.password

import com.example.manager.client.ClientService
import com.example.manager.utils.AsymmetricalCryptoUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PasswordService {
    @Autowired
    lateinit var passwordRepository:  PasswordRepository


    fun savePassword(password: Password) {
        passwordRepository.save(password)
    }

    fun getByClientId(ID : Long) : Collection<Password>{
        return passwordRepository.findByClientId(ID)
    }
}