package com.capstone.jachulsa.controller

import com.capstone.jachulsa.domain.User
import com.capstone.jachulsa.repository.UserRepository
import com.capstone.jachulsa.service.JwtTokenProvider
import com.capstone.jachulsa.service.UserService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val repository: UserRepository,
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider
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


    // GET /users/userInfo : jwtToken으로 유저 조회
    @GetMapping("/userInfo")
    fun getUserInfo(@RequestHeader("Authorization", required = true) authorizationHeader: String): User {
        val email: String = jwtTokenProvider.getEmailFromJwt(authorizationHeader)
        return userService.findUserByEmail(email)
    }

    // PUT /users/deactivate : jwtToken으로 유저 탈퇴(is_active true>false)
    @PutMapping("/deactivate")
    fun deactivateUser(@RequestHeader("Authorization", required = true) authorizationHeader: String
    ): User {
        val email: String = jwtTokenProvider.getEmailFromJwt(authorizationHeader)
        return userService.deactivateUser(email)
    }

    // PUT /users/activate : jwtTokendm로 사용자의 is_active를 true로 변경
    @PutMapping("/activate")
    fun activateUser(@RequestHeader("Authorization", required = true) authorizationHeader: String
    ): User {
        val email: String = jwtTokenProvider.getEmailFromJwt(authorizationHeader)
        return userService.activateUser(email)
    }
}