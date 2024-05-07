package com.capstone.jachulsa.repository

import com.capstone.jachulsa.domain.RidingHistory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDate

interface RidingHistoryRepository :MongoRepository<RidingHistory, String>{

    fun findByUserIdAndDateBetween(userId: String, startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<RidingHistory>

    fun findByDateBetween(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<RidingHistory>
}