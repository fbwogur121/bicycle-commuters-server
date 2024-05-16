package com.capstone.jachulsa.service

import com.capstone.jachulsa.domain.Expenditure
import com.capstone.jachulsa.dto.ResponseCode
import com.capstone.jachulsa.exception.CustomException
import com.capstone.jachulsa.repository.ExpenditureRepository
import com.capstone.jachulsa.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ExpenditureService(private val expenditureRepository: ExpenditureRepository, private val userRepository: UserRepository) {

    fun createExpenditure(expenditure: Expenditure): Expenditure {
        val user = userRepository.findById(expenditure.userId)
            .orElseThrow { CustomException(ResponseCode.USER_NOT_FOUND) }
        return expenditureRepository.save(expenditure)
    }

    fun getExpenditures(
        userId: String,
        pageable: Pageable
    ): Page<Expenditure> {

        val user = userRepository.findById(userId)
            .orElseThrow { CustomException(ResponseCode.USER_NOT_FOUND) }

        val expenditures = expenditureRepository.findByUserId(userId, pageable)
        if (expenditures.isEmpty) {
            throw CustomException(ResponseCode.EXPENDITURE_NOT_FOUND)
        }
        return expenditures
    }
}
