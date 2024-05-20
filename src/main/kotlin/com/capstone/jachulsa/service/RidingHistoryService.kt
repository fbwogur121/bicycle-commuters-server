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

    fun createRidingHistory(ridingHistory: RidingHistory): RidingHistory{
        userRepository.findOneByEmail(ridingHistory.email) ?: throw CustomException(ResponseCode.USER_NOT_FOUND)
        return ridingHistoryRepository.save(ridingHistory)
    }


    fun getRidings(
        email: String,
        myRidesOnly: Boolean,
        pageable: Pageable
    ): Page<RidingHistory> {

        userRepository.findOneByEmail(email) ?: throw CustomException(ResponseCode.USER_NOT_FOUND)

        val rides: Page<RidingHistory> = if (myRidesOnly) {
            ridingHistoryRepository.findByEmail(email, pageable)
        } else {
            ridingHistoryRepository.findAll(pageable)
        }

        if (rides.isEmpty) throw CustomException(ResponseCode.RIDING_NOT_FOUND)

        return rides
    }


    fun getRidingsByDate(userId: String, date: LocalDate): List<RidingHistory> {
        return ridingHistoryRepository.findByEmailAndDate(userId, date)?.takeIf { it.isNotEmpty() }
                ?: throw CustomException(ResponseCode.RIDING_NOT_FOUND)    }
}