package com.example.manager.password

import com.example.manager.client.Client
import com.example.manager.client.ClientService
import com.example.manager.utils.AsymmetricalCryptoUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.stereotype.Service

@Service
class PasswordService {
    @Autowired
    lateinit var passwordRepository:  PasswordRepository

    @Autowired
    lateinit var clientService: ClientService

    fun savePassword(passwordRequestData: PasswordRequestData) : Int{
        return 0
    }

    fun getByClientId(ID : Long) : Collection<Password>{
        return passwordRepository.findByClientId(ID)
    }

    fun getLogged() : HashMap<String, Client>{
        return clientService.logged
    }
}