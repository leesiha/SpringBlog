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
    private val userService: UserService
) {

    @PostMapping("/signup")
    fun signup(@RequestBody signupRequest: SignupRequest): SignupResponse {
        // Email 중복 체크는 서비스 레이어에서 처리
        val savedUser = userService.createUser(signupRequest)
        return SignupResponse(savedUser.email, savedUser.username)
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): LoginResponse {
        val user = userService.authenticate(loginRequest.email, loginRequest.password)
        return LoginResponse(user.email, user.username)
    }
}
