package com.capstone.jachulsa.repository

import com.capstone.jachulsa.domain.RidingHistory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDate

interface RidingHistoryRepository :MongoRepository<RidingHistory, String>{

    fun findByEmail(email: String, pageable: Pageable) : Page<RidingHistory>

    override fun findAll(pageable: Pageable) : Page<RidingHistory>

    fun findByEmailAndDate(email: String, date: LocalDate): List<RidingHistory>

}