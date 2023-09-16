package com.example.demo.controller

import com.example.demo.dto.request.CreateCommentRequest
import com.example.demo.dto.request.DeleteCommentRequest
import com.example.demo.dto.request.UpdateCommentRequest
import com.example.demo.dto.response.CreateCommentResponse
import com.example.demo.dto.response.UpdateCommentResponse
import com.example.demo.model.Comment
import com.example.demo.model.User
import com.example.demo.service.CommentService
import com.example.demo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/articles/{articleId}/comments")
class CommentController(
    private val commentService: CommentService,
    private val userService: UserService
) {

    @PostMapping
    fun createComment(
        @PathVariable articleId: Long,
        @RequestBody createRequest: CreateCommentRequest
    ): ResponseEntity<CreateCommentResponse> {

        val user: User = userService.authenticate(createRequest.email, createRequest.password)
        val createdComment: Comment = commentService.createComment(articleId, createRequest)

        return ResponseEntity.ok(CreateCommentResponse(createdComment.id, createdComment.user.email, createdComment.content))
    }

    @GetMapping
    fun getCommentsByArticleId(@PathVariable articleId: Long): List<Comment> {
        return commentService.findCommentsByArticleId(articleId)
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable articleId: Long,
        @PathVariable commentId: Long,
        @RequestBody updateRequest: UpdateCommentRequest
    ): ResponseEntity<UpdateCommentResponse> {

        val user: User = userService.authenticate(updateRequest.email, updateRequest.password)
        val updatedComment: Comment = commentService.updateComment(articleId, commentId, updateRequest)

        return ResponseEntity.ok(UpdateCommentResponse(commentId, user.email, updatedComment.content))
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable articleId: Long,
        @PathVariable commentId: Long,
        @RequestBody deleteRequest: DeleteCommentRequest
    ): ResponseEntity<Unit> {

        val user: User = userService.authenticate(deleteRequest.email, deleteRequest.password)
        commentService.deleteComment(articleId, commentId, deleteRequest)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}