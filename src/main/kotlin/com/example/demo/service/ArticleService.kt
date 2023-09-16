package com.example.demo.service

import com.example.demo.dto.request.CreateArticleRequest
import com.example.demo.dto.request.DeleteArticleRequest
import com.example.demo.dto.request.UpdateArticleRequest
import com.example.demo.exception.ArticleNotFoundException
import com.example.demo.model.Article
import com.example.demo.model.User
import com.example.demo.repository.ArticleRepository
import com.example.demo.repository.CommentRepository
import com.example.demo.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
@Transactional
class ArticleService(
    private val commentRepository: CommentRepository,
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository
) {
    fun findAllArticles(): List<Article> {
        return articleRepository.findAll()
    }

    fun findArticlesByEmail(email: String): List<Article> {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("No user found with email: $email")

        user.id.let {
            return articleRepository.findByUserId(it)
        }
    }

    fun findArticleById(articleId: Long): Article {
        return articleRepository.findById(articleId).orElseThrow { ArticleNotFoundException("Article with ID $articleId not found") }
    }

    fun createArticle(user:User, createRequest: CreateArticleRequest): Article {
        val article = Article().apply {
            createdAt = Instant.now()
            updatedAt = Instant.now()
            content = createRequest.content
            title = createRequest.title
            this.user = user
        }
        return articleRepository.save(article)
    }

    fun updateArticle(articleId: Long, updateRequest: UpdateArticleRequest): Article {
        val article: Article = articleRepository.findById(articleId).orElseThrow { Exception("Article not found") }
        article.title = updateRequest.title
        article.content = updateRequest.content
        article.updatedAt = Instant.now()
        return articleRepository.save(article)
    }

    fun deleteArticle(articleId: Long, authenticatedUser:User, deleteRequest: DeleteArticleRequest) {
        val article: Article = articleRepository.findById(articleId).orElseThrow { Exception("Article with ID $articleId not found") }


            if (article.user.email != authenticatedUser.email) {
                throw Exception("Only the author can delete the article")
            }

        commentRepository.deleteByArticleId(articleId)
        articleRepository.delete(article)
    }
}
