package com.example.manager.exceptions

class ApiRequestException(message : String, cause : Throwable)
        : RuntimeException(message, cause){
    constructor(message: String) : this(message, Throwable())

}