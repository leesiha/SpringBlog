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

        // 먼저 사용자 인증
        val user: User = userService.authenticate(updateRequest.email, updateRequest.password)
            ?: // 인증 실패
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val updatedUser: User = userService.updateUser(userId, updateRequest)
            ?: // 유저 정보 수정 실패 or 권한 없음
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()

        return ResponseEntity.ok(UpdateUserResponse(updatedUser.email, updatedUser.username))
    }
}


