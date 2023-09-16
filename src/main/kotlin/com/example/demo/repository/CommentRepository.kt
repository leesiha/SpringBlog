package com.example.demo.repository

import com.example.demo.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByArticleId(articleId: Long): List<Comment>
    fun findByUserEmail(email: String): List<Comment>
    fun deleteByArticleId(articleId: Long)
}
