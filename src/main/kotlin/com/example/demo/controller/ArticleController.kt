package com.example.demo.controller

import com.example.demo.dto.request.CreateArticleRequest
import com.example.demo.dto.request.DeleteArticleRequest
import com.example.demo.dto.request.UpdateArticleRequest
import com.example.demo.dto.response.CreateArticleResponse
import com.example.demo.dto.response.UpdateArticleResponse
import com.example.demo.model.Article
import com.example.demo.model.User
import com.example.demo.service.ArticleService
import com.example.demo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/articles")
class ArticleController(private val articleService: ArticleService, private  val userService: UserService) {

    @PostMapping
    fun createArticle(@RequestBody request: CreateArticleRequest): ResponseEntity<CreateArticleResponse> {
        val article = articleService.createArticle(request)
        return ResponseEntity.ok(CreateArticleResponse(article.id, article.user.email, article.title, article.content))
    }

    @PutMapping("/{articleId}")
    fun updateArticle(
        @PathVariable articleId: Long,
        @RequestBody updateRequest: UpdateArticleRequest
    ): ResponseEntity<UpdateArticleResponse> {

        // 먼저 사용자 인증
        val user: User = userService.authenticate(updateRequest.email, updateRequest.password)
            ?: // 인증 실패
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val updatedArticle: Article = articleService.updateArticle(articleId, updateRequest)
            ?: // 게시글 수정 실패 or 권한 없음
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()

        return ResponseEntity.ok(UpdateArticleResponse(articleId, user.email, updatedArticle.title, updatedArticle.content))
    }

    @DeleteMapping("/{articleId}")
    fun deleteArticle(
        @PathVariable articleId: Long,
        @RequestBody deleteRequest: DeleteArticleRequest
    ): ResponseEntity<Any> {

        // 먼저 사용자 인증
        try {
            articleService.deleteArticle(articleId, deleteRequest)
        } catch (ex: Exception) {
            // 예외 처리에 따라 적절한 응답 코드 및 메시지 반환
//            return when(ex) {
//                is ArticleNotFoundException -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
//                is UnauthorizedUserException -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.message)
//                else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the article.")
//            }
        }

        // 삭제 성공에 대한 응답 반환 (200 OK)
        return ResponseEntity.ok("Article deleted successfully!")
    }
}
