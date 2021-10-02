package com.example.manager.client


/*
    data class for sending stay-login key and pin over netword
 */
data class ClientKeyPinData(
    var key : String,
    var pinCode : String
){
    constructor() : this("", "")
}
