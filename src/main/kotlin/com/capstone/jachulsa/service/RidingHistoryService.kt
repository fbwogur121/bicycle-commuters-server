package com.capstone.jachulsa.service

import com.capstone.jachulsa.domain.RidingHistory
import com.capstone.jachulsa.repository.RidingHistoryRepository
import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class RidingHistoryService(private val ridingHistoryRepository: RidingHistoryRepository) {

    fun createRidingHistory(ridingHistory: RidingHistory): ObjectId?{
        return ridingHistoryRepository.save(ridingHistory).ridingHistoryId
    }


    fun getRidings(
        userId: String?,
        startDate: LocalDate,
        endDate: LocalDate,
        myRidesOnly: Boolean,
        pageable: Pageable
    ): Page<RidingHistory> {
        return if (userId != null && myRidesOnly ) {
            ridingHistoryRepository.findByUserIdAndDateBetween(userId, startDate, endDate, pageable)
        } else {
            ridingHistoryRepository.findByDateBetween(startDate, endDate, pageable)
        }
    }

    fun getRidingHistoryById(ridingId: String): RidingHistory? {
        return ridingHistoryRepository.findById(ridingId).orElse(null)
    }
}