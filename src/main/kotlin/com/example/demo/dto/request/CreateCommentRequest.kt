package com.example.demo.dto.request

data class CreateCommentRequest(
    val email : String,
    val password : String,
    val content: String
)
