package com.example.demo.controller

import com.example.demo.dto.request.CreateArticleRequest
import com.example.demo.dto.request.DeleteArticleRequest
import com.example.demo.dto.request.UpdateArticleRequest
import com.example.demo.dto.response.CreateArticleResponse
import com.example.demo.dto.response.UpdateArticleResponse
import com.example.demo.model.Article
import com.example.demo.model.User
import com.example.demo.repository.CommentRepository
import com.example.demo.service.ArticleService
import com.example.demo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/articles")
class ArticleController(
    private val articleService: ArticleService,
    private val commentRepository: CommentRepository
) {

    @PostMapping
    fun createArticle(@RequestBody request: CreateArticleRequest): CreateArticleResponse {
        val article = articleService.createArticle(request)
        return CreateArticleResponse(article.id, article.user.email, article.title, article.content)
    }

    /*
     * 응답의 복잡성을 축소시키기 위해 단순히 객체만을 반환하도록 했습니다.
     * 추가적인 메타데이터나 다른 정보를 포함해야 하는 경우, 별도의 응답 클래스를 만드는 것이 좋습니다.
     */
    @GetMapping("/{articleId}")
    fun readArticle(@PathVariable articleId: Long): Article {
        return articleService.findArticleById(articleId)
    }
    @GetMapping("/user/{email}")
    fun readSpecificArticles(@PathVariable email: String): List<Article> {
        return articleService.findArticlesByEmail(email)
    }
    @GetMapping
    fun readAllArticles(): List<Article> {
        return articleService.findAllArticles()
    }

    @PutMapping("/{articleId}")
    fun updateArticle(
        @PathVariable articleId: Long,
        @RequestBody updateRequest: UpdateArticleRequest
    ): UpdateArticleResponse {
        val updatedArticle: Article = articleService.updateArticle(articleId, updateRequest)
        return UpdateArticleResponse(articleId, updatedArticle.user.email, updatedArticle.title, updatedArticle.content)
    }

    @DeleteMapping("/{articleId}")
    fun deleteArticle(
        @PathVariable articleId: Long,
        @RequestBody deleteRequest: DeleteArticleRequest
    ): ResponseEntity<Unit> {
        articleService.deleteArticle(articleId, deleteRequest)
        return ResponseEntity.ok().build()
    }
}
