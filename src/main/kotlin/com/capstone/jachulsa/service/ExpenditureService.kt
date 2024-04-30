package com.capstone.jachulsa.service

import com.capstone.jachulsa.domain.Expenditure
import com.capstone.jachulsa.repository.ExpenditureRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExpenditureService(private val expenditureRepository: ExpenditureRepository) {

    @Transactional
    fun saveExpenditure(expenditure: Expenditure): ObjectId? {
        val savedExpenditure = expenditureRepository.save(expenditure)
        return savedExpenditure.expenditureId
    }
}