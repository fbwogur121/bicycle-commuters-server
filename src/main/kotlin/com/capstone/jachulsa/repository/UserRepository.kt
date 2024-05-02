package com.capstone.jachulsa.repository

import com.capstone.jachulsa.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {
    fun findByName(name: String): List<User>
}