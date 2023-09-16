package com.example.demo.controller
import com.example.demo.dto.request.UpdateUserRequest
import com.example.demo.dto.response.UpdateUserResponse
import com.example.demo.model.User
import com.example.demo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @PutMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: Long,
        @RequestBody updateRequest: UpdateUserRequest
    ): ResponseEntity<UpdateUserResponse> {

        val user: User = userService.authenticate(updateRequest.email, updateRequest.password)
        val updatedUser: User = userService.updateUser(userId, updateRequest)

        return ResponseEntity.ok(UpdateUserResponse(updatedUser.email, updatedUser.username))
    }

    @DeleteMapping("/{userId}")
    fun getUserById(userId: Long): User {
        return userService.getUserById(userId)
    }
}


