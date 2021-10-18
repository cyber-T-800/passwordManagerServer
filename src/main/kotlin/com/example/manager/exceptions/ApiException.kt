package com.example.manager.exceptions

import org.springframework.http.HttpStatus
import java.time.ZonedDateTime

data class ApiException(
    val message : String,
    val httpStatus: HttpStatus,
    val timestamp : ZonedDateTime = ZonedDateTime.now()
)
