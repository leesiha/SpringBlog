package com.example.demo.dto.response

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorResponse(
    val time: LocalDateTime,
    val status: HttpStatus,
    val message: String,
    val requestURI: String
)
