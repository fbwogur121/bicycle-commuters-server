package com.capstone.jachulsa.service

import com.capstone.jachulsa.domain.User
import com.capstone.jachulsa.exception.CustomException
import com.capstone.jachulsa.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun findUserByName(name: String) = userRepository.findByName(name)

    fun deactivateUser(email: String): User {
        val user = userRepository.findByEmail(email).firstOrNull()
                ?: throw Exception("User not found with email: $email")

        val updatedUser = user.copy(is_active = false)
        return userRepository.save(updatedUser)
    }

    fun activateUser(email: String): User {
        val user = userRepository.findByEmail(email).firstOrNull()
                ?: throw Exception("User not found with email: $email")

        val updatedUser = user.copy(is_active = true)
        return userRepository.save(updatedUser)
    }

    fun findUserByEmail(email: String): User {
        return userRepository.findOneByEmail(email) ?: throw Exception("User not found with email: $email")
    }

}
