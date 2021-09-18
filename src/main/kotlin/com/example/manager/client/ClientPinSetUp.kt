package com.example.manager.client

data class ClientPinSetUp(
    var key : String,
    var pinCode : String
){
    constructor() : this("", "")
}
