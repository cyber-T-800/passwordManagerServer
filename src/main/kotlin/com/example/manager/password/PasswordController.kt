package com.example.manager.password

import com.example.manager.client.Client
import com.example.manager.client.ClientKeyPinData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/password")
class PasswordController {

    @Autowired
    lateinit var passwordService: PasswordService


    /*
        get passwords by combination of client API key and pin code
        return null combination is invalid
     */
    @PostMapping("get")
    fun getClientsPasswords(@RequestBody clientKeyPinData: ClientKeyPinData) : Collection<Password>?{
        return passwordService.getClientsPasswords(clientKeyPinData)
    }

    /*
        endpoint for save password
        return password ID
        return 0 if stay-logged key or pin is invalid
     */
    @PostMapping("save")
    fun savePassword(@RequestBody passwordRequestData: PasswordRequestData) : Long{
        return passwordService.savePassword(passwordRequestData)
    }

}