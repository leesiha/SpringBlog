package com.example.demo.exception

import com.example.demo.dto.response.ErrorResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException, request: WebRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, ex.message ?: "", request.getDescription(false))
    }

    @ExceptionHandler(UnauthorizedUserException::class)
    fun handleUnauthorizedUserException(ex: UnauthorizedUserException, request: WebRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED, ex.message ?: "", request.getDescription(false))
    }

    @ExceptionHandler(EmailAlreadyExistsException::class)
    fun handleEmailAlreadyExistsException(ex: EmailAlreadyExistsException, request: WebRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT, ex.message ?: "", request.getDescription(false))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException, request: WebRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST, ex.message ?: "", request.getDescription(false))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception, request: WebRequest): ErrorResponse {
        return ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred", request.getDescription(false))
    }
}
