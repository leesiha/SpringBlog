package com.example.demo.dto.response

data class UpdateArticleResponse(
    val articleId: Long,
    val email: String,
    val title: String,
    val content: String
)