package com.example.manager.client

//class for sending stay login key along with client id over net
data class ClientKeyIdData(
    var id : Long,
    var key: String
){
    constructor() : this(0, "")
}
