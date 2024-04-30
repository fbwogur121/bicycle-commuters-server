package com.capstone.jachulsa.service

import com.capstone.jachulsa.domain.RidingHistory
import com.capstone.jachulsa.repository.RidingHistoryRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RidingHistoryService(private val ridingHistoryRepository: RidingHistoryRepository) {

    @Transactional
    fun saveRidingHistory(ridingHistory: RidingHistory): ObjectId?{
        val savedRidingHistory = ridingHistoryRepository.save(ridingHistory)
        return savedRidingHistory.ridingHistoryId
    }
}