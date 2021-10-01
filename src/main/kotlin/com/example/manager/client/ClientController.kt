package com.example.manager.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.netty.transport.ClientTransport

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
    fun registerSetUpPin(@RequestBody clientPinSetUp: ClientPinSetUp) : String {
        return clientService.registerSetUpPin(clientPinSetUp)
    }

    @PostMapping("login")
    fun loginClient(@RequestBody client: Client) : ClientKeyIdData{
        return clientService.loginClient(client)
    }

    @PostMapping("login/pin")
    fun loginWithPin(@RequestBody clientPinSetUp: ClientPinSetUp) : String{
        return clientService.loginWithPin(clientPinSetUp)
    }

    @GetMapping("get/logged")
    fun getLogged() : Collection<Client>{
        return clientService.getLogged()
    }
}