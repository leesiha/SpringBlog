package com.example.demo.dto.request

data class CreateArticleRequest(
    val email: String,
    val password: String,
    val title: String,
    val content: String
)
