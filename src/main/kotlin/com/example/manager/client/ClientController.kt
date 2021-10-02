package com.example.manager.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/client")
class ClientController {
    @Autowired
    lateinit var clientService: ClientService


    @GetMapping("get/{clientID}")
    fun getById(@PathVariable clientID: Long) : Client?{
        return clientService.getClient(clientID)
    }

    @GetMapping("get/all")
    fun getAll() : List<Client>{
        return clientService.getClients()
    }

    @PostMapping("register")
    fun registerClient(@RequestBody client: Client) : ClientKeyIdData {
        return clientService.registerClient(client)
    }

    //set up pin for stay-login server instance
    @PostMapping("register/pin")
    fun registerSetUpPin(@RequestBody clientPinSetUp: ClientKeyPinData) : String {
        return clientService.registerSetUpPin(clientPinSetUp)
    }

    @PostMapping("login")
    fun loginClient(@RequestBody client: Client) : ClientKeyIdData{
        return clientService.loginClient(client)
    }

    @PostMapping("login/pin")
    fun loginWithPin(@RequestBody clientPinSetUp: ClientKeyPinData) : String{
        return clientService.loginWithPin(clientPinSetUp)
    }

}