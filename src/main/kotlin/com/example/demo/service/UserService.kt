package com.example.demo.service

import com.example.demo.dto.request.*
import com.example.demo.exception.UnauthorizedUserException
import com.example.demo.exception.UserNotFoundException
import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant


@Service
class UserService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val articleService: ArticleService,
    private val commentService: CommentService
) {
    private fun encryptPassword(password: String): String {
        return passwordEncoder.encode(password)
    }

    fun authenticate(email: String, password: String): User {
        val user = userRepository.findByEmail(email) ?: throw UnauthorizedUserException("Invalid credentials.")
        if (password.isBlank() || !passwordEncoder.matches(password, user.password)) {
            throw UnauthorizedUserException("Invalid credentials.")
        }
        return user
    }

    /**
     * Check if the given email already exists in the database.
     */
    fun isEmailExists(email: String): Boolean {
        return userRepository.findByEmail(email) != null
    }

    /**
     * Find a user by their email address.
     *
     * @param email The email address to search for.
     * @return The found user or null if no user was found with the given email.
     */
    fun getUserByEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw UserNotFoundException("User with email $email not found")
    }
    @Transactional
    fun getUserById(userId: Long): User {
        return userRepository.findById(userId).orElseThrow { UserNotFoundException("User with ID $userId not found") }
    }


    /**
     * Create a new user based on the SignupRequest and save it to the database.
     */
    fun createUser(signupRequest: SignupRequest): User {
        val newUser = User().apply {
            createdAt = Instant.now()
            updatedAt = Instant.now()
            email = signupRequest.email
            password = encryptPassword(signupRequest.password)
            username = signupRequest.email
        }
        return userRepository.save(newUser)
    }

    fun updateUser(userId: Long, updateRequest: UpdateUserRequest): User {
        val user: User = userRepository.findById(userId).orElse(null)
        user.username = updateRequest.username
        user.updatedAt = Instant.now()
        return userRepository.save(user)
    }

    fun deleteUser(request: LoginRequest) {
        val user = authenticate(request.email, request.password)

        // 해당 사용자가 작성한 댓글들을 모두 찾아서 삭제
        val comments = commentService.findCommentsByEmail(user.email)
        for (comment in comments) {
            commentService.deleteComment(comment.article.id, comment.id, user, DeleteCommentRequest(request.email, request.password))
        }
        // 해당 사용자가 작성한 게시글들을 모두 찾아서 삭제
        val articles = articleService.findArticlesByEmail(user.email)
        for (article in articles) {
            articleService.deleteArticle(article.id, user, DeleteArticleRequest(request.email, request.password))
        }

        // 사용자 삭제
        userRepository.deleteById(user.id)
    }

}

