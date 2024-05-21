package com.capstone.jachulsa.repository

import com.capstone.jachulsa.controller.RidingHistoryController
import com.capstone.jachulsa.domain.RidingHistory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDate

interface RidingHistoryRepository :MongoRepository<RidingHistory, String>{

    fun findByEmail(email: String, pageable: Pageable) : Page<RidingHistory>

    override fun findAll(pageable: Pageable) : Page<RidingHistory>

    fun findByEmailAndDate(email: String, date: LocalDate): List<RidingHistory>

    @Aggregation(pipeline = [
        "{ \$match: { date: { \$gte: ?0, \$lte: ?1 } } }",
        "{ \$group: { _id: '\$email', totalDistanceMeters: { \$sum: '\$distanceMeters' }, totalRidingMinutes: { \$sum: '\$ridingMinutes' } } }",
        "{ \$sort: { totalDistanceMeters: -1 } }",
        "{ \$skip: ?#{#pageable.offset} }",
        "{ \$limit: ?#{#pageable.pageSize} }"
    ])
    fun getRankingByDistance(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): List<RidingHistoryController.RankingResponse>

    @Aggregation(pipeline = [
        "{ \$match: { date: { \$gte: ?0, \$lte: ?1 } } }",
        "{ \$group: { _id: null, totalDistanceMeters: { \$sum: '\$distanceMeters' }, totalRidingMinutes: { \$sum: '\$ridingMinutes' }, totalUsers: { \$sum: 1 }, totalReduceAmountWon: { \$sum: { \$toInt: '\$reduceAmountWon' } } } }"
    ])
    fun getTotalStatistics(startDate: LocalDate, endDate: LocalDate): RidingHistoryController.TotalStatistics
}


