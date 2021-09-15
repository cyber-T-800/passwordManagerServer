package com.example.manager.password

import com.example.manager.client.Client
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/password")
class PasswordController {

    @Autowired
    lateinit var passwordService: PasswordService

    @GetMapping
    fun get() : Password{
        return Password(0, "dsk", "kds", "dksp", Client())
    }
}