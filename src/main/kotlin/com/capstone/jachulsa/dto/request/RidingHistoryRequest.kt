package com.capstone.jachulsa.dto.request

import com.capstone.jachulsa.domain.Arrivals
import com.capstone.jachulsa.domain.Departures
import com.capstone.jachulsa.domain.RidingHistory
import com.capstone.jachulsa.domain.Stopover
import com.capstone.jachulsa.domain.enumtype.Bike
import com.capstone.jachulsa.domain.enumtype.RidingType
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class RidingHistoryRequest(
//    @field:NotBlank(message = "2001")
//    val userId: String,
    val type: RidingType,
    val date: LocalDate,
    val bike: Bike?,
    val departures: Departures,
    val arrivals: Arrivals,
    val stopover: Stopover?,
    val ridingMinutes: Int,
    val distanceMeters: Int,
    val reduceAmountWon: Int
) {
    fun toRidingHistory(email: String) : RidingHistory {
        return RidingHistory(
            email = email,
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