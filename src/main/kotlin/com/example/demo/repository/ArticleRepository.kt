package com.example.demo.repository

import com.example.demo.model.Article
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Long> {
    fun findByUserId(userId: Long): List<Article>
}