package com.example.demo.service

import com.example.demo.dto.request.SignupRequest
import com.example.demo.exception.UnauthorizedUserException
import com.example.demo.exception.UserNotFoundException
import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import com.nhaarman.mockitokotlin2.whenever
import org.mockito.ArgumentMatchers.any
import java.util.*

class UserServiceTest {

    private lateinit var userService: UserService

    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var userRepository: UserRepository
    private lateinit var articleService: ArticleService
    private lateinit var commentService: CommentService

    @BeforeEach
    fun setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder::class.java)
        userRepository = Mockito.mock(UserRepository::class.java)
        articleService = Mockito.mock(ArticleService::class.java)
        commentService = Mockito.mock(CommentService::class.java)
        userService = UserService(passwordEncoder, userRepository, articleService, commentService)
    }

    private fun User(email:String, pw:String): User {
        return User().apply {
            this.email=email
            password=pw
            createdAt=Instant.now()
            updatedAt=Instant.now()
            username=email
        }
    }

    // test authenticate
    @Test
    fun `should authenticate valid user`() {
        // Setup mock behavior for valid user
        val validUser = User("test@example.com", "encryptedPassword")

        whenever(userRepository.findByEmail("test@example.com")).thenReturn(validUser)
        whenever(passwordEncoder.matches("password", "encryptedPassword")).thenReturn(true)

        // Testing valid user authentication
        val authenticatedUser = userService.authenticate("test@example.com", "password")

        assertNotNull(authenticatedUser)
        assertEquals("test@example.com", authenticatedUser.email)
    }

    @Test
    fun `should throw UnauthorizedUserException for invalid user`() {
        // Setup mock behavior for invalid user
        whenever(userRepository.findByEmail("invalid@example.com")).thenReturn(null)

        // Testing invalid user authentication
        assertThrows<UnauthorizedUserException> {
            userService.authenticate("invalid@example.com", "password")
        }
    }


    // test isEmailExists
    @Test
    fun `should return true when email exists`() {
        // Setup mock behavior for existing email
        val existingUser = User("existing@example.com", "someEncryptedPassword")
        `when`(userRepository.findByEmail("existing@example.com")).thenReturn(existingUser)

        // Testing if email exists
        val result = userService.isEmailExists("existing@example.com")

        assertTrue(result, "Expected true when email exists, but got false.")
    }
    @Test
    fun `should return false when email does not exist`() {
        // Setup mock behavior for non-existing email
        `when`(userRepository.findByEmail("nonExisting@example.com")).thenReturn(null)

        // Testing if email doesn't exist
        val result = userService.isEmailExists("nonExisting@example.com")

        assertFalse(result, "Expected false when email doesn't exist, but got true.")
    }

    // test getUserByEmail
    @Test
    fun `should return user when email exists`() {
        // Setup mock behavior for existing email
        val existingUser = User("existing@example.com", "someEncryptedPassword")
        `when`(userRepository.findByEmail("existing@example.com")).thenReturn(existingUser)

        // Testing fetching user by existing email
        val returnedUser = userService.getUserByEmail("existing@example.com")

        assertEquals(existingUser, returnedUser, "Returned user does not match the expected user.")
    }

    @Test
    fun `should throw UserNotFoundException when email does not exist`() {
        // Setup mock behavior for non-existing email
        `when`(userRepository.findByEmail("nonExisting@example.com")).thenReturn(null)

        // Testing fetching user by non-existing email
        assertThrows<UserNotFoundException> {
            userService.getUserByEmail("nonExisting@example.com")
        }
    }


    // test getUserById
    @Test
    fun `should return user when user ID exists`() {
        // Setup mock behavior for existing user ID
        val existingUser = User("user@example.com", "someEncryptedPassword")
        `when`(userRepository.findById(existingUser.id)).thenReturn(Optional.of(existingUser))

        // Testing fetching user by existing user ID
        val returnedUser = userService.getUserById(existingUser.id!!)

        assertEquals(existingUser, returnedUser, "Returned user does not match the expected user.")
    }

    @Test
    fun `should throw UserNotFoundException when user ID does not exist`() {
        // Setup mock behavior for non-existing user ID
        val nonExistingUserId = 99L
        `when`(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty())

        // Testing fetching user by non-existing user ID
        assertThrows<UserNotFoundException> {
            userService.getUserById(nonExistingUserId)
        }
    }



    // test createUser
    @Test
    fun `should create user with valid signup request`() {
        // Prepare the SignupRequest data
        val signupRequest = SignupRequest("test@example.com", "testPassword", "test@example.com")

        // Mock the repository save function to return the user it's given
        `when`(userRepository.save(any(User::class.java))).thenAnswer { it.arguments[0] }

        // Call the createUser function
        val createdUser = userService.createUser(signupRequest)

        // Assertions to validate the created user
        assertEquals(signupRequest.email, createdUser.email)
        assertEquals(passwordEncoder.encode(signupRequest.password), createdUser.password)  // Assuming encryptPassword is accessible
        assertNotNull(createdUser.createdAt)
        assertNotNull(createdUser.updatedAt)
        assertEquals(signupRequest.email, createdUser.username)
    }


    // test updateUser


    // test deleteUser

}
