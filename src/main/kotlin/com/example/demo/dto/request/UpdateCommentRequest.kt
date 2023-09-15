package com.example.demo.dto.request

data class UpdateCommentRequest(
    val email: String,
    val password: String,
    val content: String
)
