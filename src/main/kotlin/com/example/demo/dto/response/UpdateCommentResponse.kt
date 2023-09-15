package com.example.demo.dto.response

data class UpdateCommentResponse(
    val commentId: Long,
    val email: String,
    val content: String
)
