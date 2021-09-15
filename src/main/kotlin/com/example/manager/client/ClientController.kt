package com.example.manager.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/client")
class ClientController {
    @Autowired
    lateinit var clientService: ClientService

    @GetMapping("get")
    fun get(@PathVariable id: Long) : Client{
        return clientService.getClient(id)
    }

    @GetMapping("get/all")
    fun getAll() : List<Client>{
        return clientService.getClients()
    }

    @PostMapping("register")
    fun registerClient(@RequestBody client: Client) : Boolean {
        return clientService.registerClient(client)
    }
}