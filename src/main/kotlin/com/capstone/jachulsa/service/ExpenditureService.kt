package com.capstone.jachulsa.service

import com.capstone.jachulsa.domain.Expenditure
import com.capstone.jachulsa.repository.ExpenditureRepository
import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ExpenditureService(private val expenditureRepository: ExpenditureRepository) {

    fun createExpenditure(expenditure: Expenditure):ObjectId? {
        return expenditureRepository.save(expenditure).expenditureId
    }

    fun getExpenditures(userId: String, startDate: LocalDate, endDate: LocalDate,  pageable: Pageable): Page<Expenditure> { //myExpenditureOnly: Boolean,
        return expenditureRepository.findByUserIdAndDateBetween(userId, startDate, endDate, pageable)
    }
}