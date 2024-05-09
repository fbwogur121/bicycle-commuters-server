package com.capstone.jachulsa.service

import com.capstone.jachulsa.domain.RidingHistory
import com.capstone.jachulsa.dto.ResponseCode
import com.capstone.jachulsa.exception.CustomException
import com.capstone.jachulsa.repository.RidingHistoryRepository
import com.capstone.jachulsa.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.time.LocalDate

@Service
class RidingHistoryService(private val ridingHistoryRepository: RidingHistoryRepository, private val userRepository: UserRepository) {

    fun createRidingHistory(ridingHistory: RidingHistory): ObjectId?{
        val user = userRepository.findById(ridingHistory.userId)
            .orElseThrow { CustomException(ResponseCode.USER_NOT_FOUND) }
        return ridingHistoryRepository.save(ridingHistory).ridingHistoryId
    }


    fun getRidings(
        userId: String?,
        startDate: LocalDate,
        endDate: LocalDate,
        myRidesOnly: Boolean,
        pageable: Pageable
    ): Page<RidingHistory> {

        val user = userId?.let {
            userRepository.findById(it)
                .orElseThrow { CustomException(ResponseCode.USER_NOT_FOUND) }
        }

        val rides: Page<RidingHistory> = if (userId != null && myRidesOnly) {
            ridingHistoryRepository.findByUserIdAndDateBetween(userId, startDate, endDate, pageable)
        } else {
            ridingHistoryRepository.findByDateBetween(startDate, endDate, pageable)
        }

        if (rides.isEmpty) throw CustomException(ResponseCode.RIDING_NOT_FOUND)

        return rides
    }

    fun getRidingHistoryById(ridingId: String): RidingHistory? {
        return ridingHistoryRepository.findById(ridingId).orElseThrow {
            CustomException(ResponseCode.RIDING_NOT_FOUND)
        }
    }
}