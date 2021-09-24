package com.example.manager.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/client")
class ClientController {
    @Autowired
    lateinit var clientService: ClientService

    @GetMapping("get/{clientID}")
    fun get(@PathVariable clientID: Long) : Client?{
        return clientService.getClient(clientID)
    }

    @GetMapping("get/all")
    fun getAll() : List<Client>{
        return clientService.getClients()
    }

    @PostMapping("register")
    fun registerClient(@RequestBody client: Client) : String {
        return clientService.registerClient(client)
    }

    @PostMapping("register/pin")
    fun registerSetUpPin(@RequestBody clientPinSetUp: ClientPinSetUp) : String{
        return clientService.registerSetUpPin(clientPinSetUp)
    }

    @PostMapping("login")
    fun loginClient(@RequestBody client: Client) : String{
        return clientService.loginClient(client)
    }

    @PostMapping("login/pin")
    fun loginWithPin(@RequestBody clientPinSetUp: ClientPinSetUp) : String{
        return clientService.loginWithPin(clientPinSetUp)
    }
}