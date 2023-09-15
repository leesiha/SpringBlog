package com.example.demo.controller

import com.example.demo.dto.request.LoginRequest
import com.example.demo.dto.request.SignupRequest
import com.example.demo.dto.response.LoginResponse
import com.example.demo.dto.response.SignupResponse
import com.example.demo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) {
    @PostMapping("/signup")
    fun signup(@RequestBody signupRequest: SignupRequest): ResponseEntity<Any> {
        // Check if the email already exists
        if (userService.isEmailExists(signupRequest.email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.")
        }
        val savedUser = userService.createUser(signupRequest)
        return ResponseEntity.ok(SignupResponse(savedUser.email, savedUser.username))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        val user = userService.authenticate(loginRequest.email, loginRequest.password)
        return ResponseEntity.ok(LoginResponse(user.email, user.username))
    }

    @PostMapping("/logout")
    fun logout() {
        // Depending on your authentication method, this can invalidate sessions, tokens, etc.
        // For example, if you're using JWTs, you might maintain a token blacklist.
    }
}