package com.example.demo.controller

import com.example.demo.dto.request.CreateCommentRequest
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
@RequestMapping("/comments")
class CommentController(
    private val commentService: CommentService,
    private val userService: UserService
) {
    @PutMapping("/{articleId}")
    fun createComment(
        @PathVariable articleId: Long,
        @RequestBody createRequest: CreateCommentRequest
    ): ResponseEntity<CreateCommentResponse> {

        // 먼저 사용자 인증
        val user: User = userService.authenticate(createRequest.email, createRequest.password)
            ?: // 인증 실패
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val createdComment: Comment = commentService.createComment(articleId, createRequest)
            ?: // 게시글 수정 실패 or 권한 없음
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()

        return ResponseEntity.ok(CreateCommentResponse(createdComment.id, createdComment.user.email, createdComment.content))
    }

    @PutMapping("/{articleId}/{commentId}")
    fun updateComment(
        @PathVariable articleId: Long,
        @PathVariable commentId: Long,
        @RequestBody updateRequest: UpdateCommentRequest
    ): ResponseEntity<UpdateCommentResponse> {

        // 먼저 사용자 인증
        val user: User = userService.authenticate(updateRequest.email, updateRequest.password)
            ?: // 인증 실패
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val updatedComment: Comment = commentService.updateComment(articleId, commentId, updateRequest)
        return ResponseEntity.ok(UpdateCommentResponse(commentId, user.email, updatedComment.content))
    }


}
