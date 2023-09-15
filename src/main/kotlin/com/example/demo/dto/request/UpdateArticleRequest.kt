package com.example.demo.dto.request

data class UpdateArticleRequest(
    val email: String,
    val password: String,
    val title: String,
    val content: String
)