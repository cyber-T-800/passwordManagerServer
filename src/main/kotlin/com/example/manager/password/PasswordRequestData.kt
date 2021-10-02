package com.example.manager.password

import com.example.manager.client.ClientKeyPinData


/*
    data class for password http request
    store password alongside with client data
 */
data class PasswordRequestData(
    var clientKeyPinData: ClientKeyPinData,
    var password: Password
){
    constructor() : this(ClientKeyPinData(), Password())
}
