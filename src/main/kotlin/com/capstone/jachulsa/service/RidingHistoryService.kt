package com.capstone.jachulsa.service

import com.capstone.jachulsa.controller.RidingHistoryController
import com.capstone.jachulsa.domain.RidingHistory
import com.capstone.jachulsa.dto.ResponseCode
import com.capstone.jachulsa.exception.CustomException
import com.capstone.jachulsa.repository.ExpenditureRepository
import com.capstone.jachulsa.repository.RidingHistoryRepository
import com.capstone.jachulsa.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class RidingHistoryService(private val ridingHistoryRepository: RidingHistoryRepository,
                           private val userRepository: UserRepository,
                           private val expenditureRepository: ExpenditureRepository) {

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

    fun getRidingsByDate(email: String, date: LocalDate): List<RidingHistory> {
        return ridingHistoryRepository.findByEmailAndDate(email, date)
//        return ridingHistoryRepository.findByEmailAndDate(email, date)?.takeIf { it.isNotEmpty() }
//                ?: throw CustomException(ResponseCode.RIDING_NOT_FOUND)    }
    }


    fun getTotalStatisticsByDate(email: String, date: LocalDate): RidingHistoryController.TotalDayStatistics {
        val ridings = ridingHistoryRepository.findByEmailAndDate(email, date)
        val totalDistance = ridings.sumOf { it.distanceMeters }
        val totalTime = ridings.sumOf { it.ridingMinutes }
        val totalreduce = ridings.sumOf { it.reduceAmountWon ?:0 }

        val expenditures = expenditureRepository.findByEmailAndDate(email, date)
        val totalExpenditure = expenditures.sumOf { it.expenditureAmountWon }

        return RidingHistoryController.TotalDayStatistics(
                totalDistanceMeters = totalDistance,
                totalRidingMinutes = totalTime,
                totalExpenditure = totalExpenditure,
                totalReduceAmountWon = totalreduce,
                totalCo2Grams = 0.021 * totalDistance
        )
    }

    fun getRankingByDistance(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): List<RidingHistoryController.RankingResponse> {
        return ridingHistoryRepository.getRankingByDistance(startDate, endDate, pageable)
    }

    fun getTotalStatistics(startDate: LocalDate, endDate: LocalDate): RidingHistoryController.TotalStatistics {
        return ridingHistoryRepository.getTotalStatistics(startDate, endDate)
    }

}