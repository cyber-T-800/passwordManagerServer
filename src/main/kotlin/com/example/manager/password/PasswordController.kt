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

    /*
        delete password from database
        return 0 operation successful
        return 1 if client stay-login data are invalid
        return 2 if password don't belong to requested client
     */
    @PostMapping("delete")
    fun deletePassword(@RequestBody passwordRequestData: PasswordRequestData) : Long{
        return passwordService.deletePassword(passwordRequestData)
    }
    /*
        edit password in database
        return 0 operation successful
        return 1 if client stay-login data are invalid
        return 2 if password don't belong to requested client
    */
    @PostMapping("edit")
    fun editPassword(@RequestBody passwordRequestData: PasswordRequestData) : Long{
        return passwordService.editPassword(passwordRequestData)
    }
}