package com.capstone.jachulsa.service

import com.capstone.jachulsa.domain.Expenditure
import com.capstone.jachulsa.dto.ResponseCode
import com.capstone.jachulsa.exception.CustomException
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
        val expenditures = expenditureRepository.findByUserIdAndDateBetween(userId, startDate, endDate, pageable)
        if (expenditures.isEmpty) {
            throw CustomException(ResponseCode.RESOURCE_NOT_FOUND)
        }
        return expenditures}
}