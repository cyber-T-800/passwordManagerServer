package com.example.manager.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(value  = [ApiRequestException::class])
    fun handleApiRequestException(e : ApiRequestException) : ResponseEntity<ApiException>{
        val apiException = ApiException(
            e.message!!,
            HttpStatus.BAD_REQUEST
        )
        return ResponseEntity(apiException, HttpStatus.BAD_REQUEST)
    }
}