package com.capstone.jachulsa.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate


@Document(collection = "RidingHistory")
data class RidingHistory(

        @Id
        val ridingHistoryId: ObjectId? = null,
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
)

data class Departures(
        val longitude: String,
        val latitude: String,
        val detailAddress: String
)

data class Arrivals(
        val longitude: String,
        val latitude: String,
        val detailAddress: String
)

data class Stopover(
        val longitude: String?,
        val latitude: String?,
        val detailAddress: String?
)