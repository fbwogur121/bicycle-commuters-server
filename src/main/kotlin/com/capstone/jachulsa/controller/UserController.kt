package com.capstone.jachulsa.controller

import com.capstone.jachulsa.domain.User
import com.capstone.jachulsa.repository.UserRepository
import com.capstone.jachulsa.service.JwtTokenProvider
import com.capstone.jachulsa.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val repository: UserRepository,
    private val userService: UserService
) {

    // GET /users : 모든 사용자 조회
    @GetMapping
    fun getAllUsers(): List<User> {
        return repository.findAll()
    }

    // GET /users/{id} : 주어진 ID를 가진 사용자를 조회
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): User? {
        return repository.findById(id).orElse(null)
    }

    // POST /users : 새 유저 생성(json형식으로 요청)
    @PostMapping
    fun createUser(@RequestBody user: User): User {
        return repository.save(user)
    }

//    // PUT /users{id} : 주어진 ID를 가진 사용자의 정보를 업데이트
//    @PutMapping("/{id}")
//    fun updateUser(@PathVariable id: String, @RequestBody updatedUser: User): User {
//        return repository.findById(id).map { existingUser ->
//            val updated: User = existingUser.copy(
//                    email = updatedUser.email,
//                    name = updatedUser.name,
//                    nickname = updatedUser.nickname,
//                    sex = updatedUser.sex,
//                    birthdate = updatedUser.birthdate,
//                    is_active = updatedUser.is_active,
//                    is_public = updatedUser.is_public,
//                    address = updatedUser.address,
//                    total_riding = updatedUser.total_riding
//            )
//            repository.save(updated)
//        }.orElseThrow { Exception("User not found") }
//    }

    // DELETE /users/{id}: 주어진 ID를 가진 사용자 삭제
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): String {
        return repository.findById(id).map { existingUser ->
            repository.delete(existingUser)
            "User deleted successfully"
        }.orElseThrow { Exception("User not found") }
    }

    // GET /users/name/{name} : 주어진 name으로 유저 조회
    @GetMapping("/name/{name}")
    fun getUsersByName(@PathVariable name: String): List<User> {
        return repository.findByName(name)
    }


    // POST /users/name : 주어진 name으로 유저 조회
    @PostMapping("/name")
    fun getUsersByName(@RequestBody request: Map<String, String>): List<User> {
        val name = request["name"]
        return if (name != null) {
            repository.findByName(name)
        } else {
            emptyList()
        }
    }

    // PUT /users/deactivate : 주어진 email로 유저 탈퇴(is_active true>false)
    @PutMapping("/deactivate")
    fun deactivateUser(@RequestBody request: Map<String, String>): User {
        val email = request["email"] ?: throw Exception("Email is required")
        return userService.deactivateUser(email)
    }

    // PUT /users/activate : 주어진 email로 사용자의 is_active를 true로 변경
    @PutMapping("/activate")
    fun activateUser(@RequestBody request: Map<String, String>): User {
        val email = request["email"] ?: throw Exception("Email is required")
        return userService.activateUser(email)
    }


    // POST /users/verify : JWT 토큰 검증 및 이메일 추출
    @PostMapping("/verify")
    fun verifyTokenAndGetEmail(@RequestBody request: Map<String, String>): ResponseEntity<Any> {
        val token = request["token"] ?: return ResponseEntity.badRequest().body(mapOf("error" to "토큰이 요청 본문에 없습니다."))

        if (JwtTokenProvider.validateJwt(token)) {
            val email = JwtTokenProvider.getEmailFromJwt(token)
            return if (email != null) {
                ResponseEntity.ok(mapOf("email" to email))
            } else {
                ResponseEntity.badRequest().body(mapOf("error" to "토큰에서 이메일을 추출할 수 없습니다."))
            }
        } else {
            return ResponseEntity.status(401).body(mapOf("error" to "유효하지 않은 토큰입니다."))
        }
    }
}