package com.example.demo.dto.request

data class SignupRequest(
    val email: String,
    val password: String,
    val username: String
)