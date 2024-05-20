package com.capstone.jachulsa.domain

import com.capstone.jachulsa.domain.enumtype.Bike
import com.capstone.jachulsa.domain.enumtype.RidingType
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate


@Document(collection = "RidingHistory")
data class RidingHistory(

        @Id
        val ridingHistoryId: ObjectId? = null,
        val email: String,
        val type: RidingType,
        val date: LocalDate,
        val bike: Bike?,
        val departures: Departures,
        val arrivals: Arrivals,
        val stopover: Stopover?,
        val ridingMinutes: Int,
        val distanceMeters: Int,
        val reduceAmountWon: Int?
)

data class Departures(
        val longitude: String?,
        val latitude: String?,
        val detailAddress: String
)

data class Arrivals(
        val longitude: String?,
        val latitude: String?,
        val detailAddress: String
)

data class Stopover(
        val longitude: String?,
        val latitude: String?,
        val detailAddress: String?
)