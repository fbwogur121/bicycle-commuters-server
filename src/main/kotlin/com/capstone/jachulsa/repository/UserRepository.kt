package com.capstone.jachulsa.repository

import com.capstone.jachulsa.domain.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String>{
    fun findByName(name: String): List<User>
    fun findByEmail(email: String): List<User>
    fun findOneByEmail(email: String) : User?

}