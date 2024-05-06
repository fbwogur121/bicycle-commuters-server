package com.capstone.jachulsa.service

import com.capstone.jachulsa.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun findUserByName(name: String) = userRepository.findByName(name)
}