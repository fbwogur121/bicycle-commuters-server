package com.capstone.jachulsa.dto.request

import com.capstone.jachulsa.domain.Arrivals
import com.capstone.jachulsa.domain.Departures
import com.capstone.jachulsa.domain.RidingHistory
import com.capstone.jachulsa.domain.Stopover
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class RidingHistoryRequest(
    @field:NotBlank(message = "2001")
    val userId: String,
    @field:NotBlank(message = "2001")
    val type: String,
    val date: LocalDate,
    @field:NotBlank(message = "2001")
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