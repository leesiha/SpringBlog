package com.example.demo.dto.request

data class UpdateUserRequest(
    val email: String,
    val password: String,
    val username: String
)
