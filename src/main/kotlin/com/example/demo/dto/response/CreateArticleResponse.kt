package com.example.demo.dto.response

data class CreateArticleResponse(
    val articleId: Long,
    val email: String,
    val title: String,
    val content: String
)
