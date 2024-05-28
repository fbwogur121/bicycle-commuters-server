package com.capstone.jachulsa.repository

import com.capstone.jachulsa.domain.Expenditure
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import java.time.LocalDate

interface ExpenditureRepository :MongoRepository<Expenditure, String>{
   // fun findByUserIdAndDateBetween(userId: String, startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<Expenditure>

//    fun findByUserId(userId: String, pageable: Pageable) : Page<Expenditure>

    fun findByEmail(email: String, pageable: Pageable) : Page<Expenditure>

    fun findByEmailAndDate(email: String, date: LocalDate): List<Expenditure>

}