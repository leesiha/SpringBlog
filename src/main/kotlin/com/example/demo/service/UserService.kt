package com.example.demo.service

import com.example.demo.dto.request.SignupRequest
import com.example.demo.dto.request.UpdateUserRequest
import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import javax.naming.AuthenticationException


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    private fun encryptPassword(password: String): String {
        return passwordEncoder.encode(password)
    }

    fun authenticate(email: String, password: String): User {
        val user = userRepository.findByEmail(email) ?: throw AuthenticationException("Invalid email.")
        if (!passwordEncoder.matches(password, user.password)) {
            throw AuthenticationException("Invalid password.")
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
    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
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
}

