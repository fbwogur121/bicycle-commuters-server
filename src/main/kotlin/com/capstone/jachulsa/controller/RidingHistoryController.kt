package com.capstone.jachulsa.controller

import com.capstone.jachulsa.domain.Arrivals
import com.capstone.jachulsa.domain.Departures
import com.capstone.jachulsa.domain.RidingHistory
import com.capstone.jachulsa.domain.Stopover
import com.capstone.jachulsa.service.RidingHistoryService
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/riding")
class RidingHistoryController (private val service: RidingHistoryService){

    // POST /riding : 라이딩 생성
    @PostMapping
    fun createRidingHistory(@RequestBody request: RidingHistoryRequest): RidingHistoryResponse {
        val ridingHistory = request.toRidingHistory()
        val ridingHistoryId = service.saveRidingHistory(ridingHistory)
        return RidingHistoryResponse(ridingHistoryId.toString())
    }

    data class RidingHistoryRequest(
        val userId: String,
        val type: String,
        val date: LocalDate,
        val bike: String?,
        val departures: Departures,
        val arrivals: Arrivals,
        val stopover: Stopover?,
        val ridingMinutes: Int,
        val distanceMeters: Int,
        val reduceAmountWon: Int?
    ) {
        fun toRidingHistory() : RidingHistory {
            return RidingHistory(
                userId = this.userId,
                type = this.type,
                date = this.date,
                bike = this.bike,
                departures = this.departures,
                arrivals = this.arrivals,
                stopover = this.stopover,
                ridingMinutes = this.ridingMinutes,
                distanceMeters = this.distanceMeters,
                reduceAmountWon = this.reduceAmountWon
            )
        }
    }

    data class RidingHistoryResponse(val ridingHistoryId: String?)

    //라이딩 상세조회

    //랭킹 조회
}