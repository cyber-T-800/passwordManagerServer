package com.example.manager.password

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PasswordService {
    @Autowired
    lateinit var passwordRepository:  PasswordRepository

}