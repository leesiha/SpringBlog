package com.example.demo.dto.response

data class CreateCommentResponse(
    val commentId: Long,
    val email: String,
    val content: String
)
