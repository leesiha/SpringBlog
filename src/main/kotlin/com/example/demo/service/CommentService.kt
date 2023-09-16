package com.example.demo.service

import com.example.demo.dto.request.CreateCommentRequest
import com.example.demo.dto.request.DeleteCommentRequest
import com.example.demo.dto.request.UpdateCommentRequest
import com.example.demo.model.Article
import com.example.demo.model.Comment
import com.example.demo.model.User
import com.example.demo.repository.CommentRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val articleService: ArticleService
) {
    fun findCommentsByArticleId(articleId: Long): List<Comment> {
        return commentRepository.findByArticleId(articleId)
    }
    fun findCommentsByEmail(email: String): List<Comment> {
        return commentRepository.findByUserEmail(email)
    }

    fun createComment(articleId: Long, user:User, createRequest: CreateCommentRequest): Comment {
        val article: Article = articleService.findArticleById(articleId)!!

        val comment = Comment().apply {
            createdAt = Instant.now()
            updatedAt = Instant.now()
            content = createRequest.content
            this.user = user
            this.article = article
        }
        return commentRepository.save(comment)
    }


    fun updateComment(articleId: Long, commentId: Long, updateRequest: UpdateCommentRequest): Comment {
        // 댓글을 찾는다.
        val comment: Comment = commentRepository.findById(commentId).orElseThrow { Exception("Comment not found") }

        // 댓글의 게시글 ID가 제공된 articleId와 일치하는지 확인한다.
        if (comment.article.id != articleId) {
            throw Exception("Comment does not belong to the provided article!")
        }

        // 사용자 인증 (댓글 작성자와 수정하려는 사용자가 동일한지 확인)
        if (comment.user.email != updateRequest.email) {
            throw Exception("Logged in user is not the author of the comment!")
        }

        comment.content = updateRequest.content
        comment.updatedAt = Instant.now()
        return commentRepository.save(comment)
    }

    fun deleteComment(articleId: Long, commentId: Long, authenticatedUser:User, deleteRequest: DeleteCommentRequest) {
        val comment: Comment = commentRepository.findById(commentId).orElseThrow { Exception("Comment with ID $commentId not found") }
        if (comment.user.email != authenticatedUser.email) {
            throw Exception("Only the author can delete the comment")
        }

        commentRepository.delete(comment)
    }
}
