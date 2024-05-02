//package com.capstone.jachulsa.domain
//
//import org.springframework.data.annotation.Id
//import org.springframework.data.mongodb.core.mapping.Document
//import java.time.LocalDate
//
//@Document(collection = "Expenses")
//data class RidingHistory(
//        @Id
//        val rudubg_id: String,
//        val user_id: String,
//        val type: String,
//        val date: LocalDate,
//        val bike: String?,
//        val departures: Departures,
//        val arrivals: Arrivals,
//        val stopover: Stopover?,
//        val riding_minutes: Int,
//        val distance_meters: Int,
//        val reduce_amount_won: Int?
//)
//
//data class Departures(
//        val longitude: String,
//        val latitude: String,
//        val detail_address: String
//)
//
//data class Arrivals(
//        val longitude: String,
//        val latitude: String,
//        val detail_address: String
//)
//
//data class Stopover(
//        val longitude: String?,
//        val latitude: String?,
//        val detail_address: String?
//)