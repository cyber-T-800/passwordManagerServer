package com.example.manager.password

import com.example.manager.client.Client
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/password")
class PasswordController {

    @Autowired
    lateinit var passwordService: PasswordService

    @GetMapping("get/{clientId}")
    fun getByClientId(@PathVariable clientId : Long) : Collection<Password>{
        return passwordService.getByClientId(clientId)
    }
    /*
        endpoint for save password
        return 0 if password is saved successfully
     */
    @PostMapping("save")
    fun savePassword(@RequestBody passwordRequestData: PasswordRequestData) : Int{
        return passwordService.savePassword(passwordRequestData)
    }
}