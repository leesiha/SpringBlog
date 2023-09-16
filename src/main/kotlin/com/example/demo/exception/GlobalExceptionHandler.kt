package com.example.demo.exception

import com.example.demo.dto.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, ex.message ?: "", request.getDescription(false)),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(UnauthorizedUserException::class)
    fun handleUnauthorizedUserException(ex: UnauthorizedUserException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED, ex.message ?: "", request.getDescription(false)),
            HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred.", request.getDescription(false)),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}
