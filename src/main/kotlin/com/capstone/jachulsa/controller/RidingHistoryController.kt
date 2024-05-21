package com.capstone.jachulsa.controller

import com.capstone.jachulsa.domain.RidingHistory
import com.capstone.jachulsa.domain.enumtype.Bike
import com.capstone.jachulsa.domain.enumtype.RidingType
import com.capstone.jachulsa.dto.ApiResponse
import com.capstone.jachulsa.dto.ResponseCode
import com.capstone.jachulsa.dto.request.RidingHistoryRequest
import com.capstone.jachulsa.service.JwtTokenProvider
import com.capstone.jachulsa.service.RidingHistoryService
import com.capstone.jachulsa.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate


@Tag(name = "RidingHistory API", description = "라이딩 API")
@RestController
@RequestMapping
class RidingHistoryController (private val ridingHistoryService: RidingHistoryService,
                               private val userService: UserService,
                               private val jwtTokenProvider: JwtTokenProvider){

    // POST /riding : 라이딩 생성
    @Operation(summary = "라이딩 생성", description = "jwt 유저의 라이딩기록을 생성")
    @PostMapping("/riding")
    fun createRidingHistory(@Validated @RequestBody request: RidingHistoryRequest,
                            @Validated @RequestHeader("Bearer") token: String
    ): ApiResponse<RidingResponse> {

        val email: String = jwtTokenProvider.getEmailFromJwt(token)

        val ridingHistory = ridingHistoryService.createRidingHistory(request.toRidingHistory(email))
        val ridingResponse = mapToResponse(ridingHistory)

        return ApiResponse.success(ResponseCode.CREATE_SUCCESS, ridingResponse)
    }


    //라이딩 리스트 조회
    @Operation(summary = "라이딩 기록 조회", description = "라이딩 기록을 페이징 처리 후 반환. myRidesOnly = true일 시 해당 유저의 라이딩 기록만 조회")
    @Parameters(
            Parameter(name = "myRidesOnly", description = "내 라이딩만 조회"),
            Parameter(name = "page", description = "페이지 수"),
            Parameter(name = "size", description = "페이징 사이즈")
    )
    @GetMapping("/ridings")
    fun getRidings(
            @RequestHeader("Bearer") token: String,
            @RequestParam("page") page: Int,
            @RequestParam("size") size: Int,
            @RequestParam("myRidesOnly", defaultValue = "true") myRidesOnly: Boolean,
//            @PathVariable(required = false) userId: String?
    ): ApiResponse<RidingListResponse> {

        val email: String = jwtTokenProvider.getEmailFromJwt(token)
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("date").descending())
        val ridingsPage = ridingHistoryService.getRidings(email, myRidesOnly, pageable)
        val ridings = ridingsPage.content.map { riding -> mapToResponse(riding) }

        val ridingListResponse = RidingListResponse(
                currentPage = ridingsPage.number + 1, // Page number is zero-based
                totalPages = ridingsPage.totalPages,
                totalItems = ridingsPage.totalElements,
                ridings = ridings
        )

        return ApiResponse.success(ResponseCode.READ_SUCCESS, ridingListResponse)
    }


    //라이딩 상세조회 - 일자별로
    @Operation(summary = "라이딩 상세 조회", description = "")
    @GetMapping("/riding")
    fun getRidingHistory(@Validated @RequestHeader("Bearer") token: String,
                         @Validated @RequestParam("date") date: LocalDate // 날짜(YYYY-MM-DD 형식)
    ): ApiResponse<RidingDateListResponse> {

        val email: String = jwtTokenProvider.getEmailFromJwt(token)

        val ridingList = ridingHistoryService.getRidingsByDate(email, date)

        val ridingListResponse = RidingDateListResponse(
                ridings = ridingList.map { mapToResponse(it) }
        )
        return ApiResponse.success(ResponseCode.READ_SUCCESS, ridingListResponse)
    }






    private fun mapToResponse(ridingHistory: RidingHistory): RidingResponse {
        return RidingResponse(
                ridingId = ridingHistory.ridingHistoryId.toString(),
                email = ridingHistory.email,
                type = ridingHistory.type,
                date = ridingHistory.date,
                bike = ridingHistory.bike,
                departures = DeparturesResponse(
                        longitude = ridingHistory.departures.longitude,
                        latitude = ridingHistory.departures.latitude,
                        detailAddress = ridingHistory.departures.detailAddress
                ),
                arrivals = ArrivalsResponse(
                        longitude = ridingHistory.arrivals.longitude,
                        latitude = ridingHistory.arrivals.latitude,
                        detailAddress = ridingHistory.arrivals.detailAddress
                ),
                stopover = StopoverResponse(
                            longitude = ridingHistory.stopover?.longitude,
                            latitude = ridingHistory.stopover?.latitude,
                            detailAddress = ridingHistory.stopover?.detailAddress
                    )
                ,
                ridingMinutes = ridingHistory.ridingMinutes,
                distanceMeters = ridingHistory.distanceMeters,
                reduceAmountWon = ridingHistory.reduceAmountWon
        )
    }

    data class RidingResponse(
        val ridingId: String? = null,
        val email: String? = null,
        val type: RidingType? = null,
        val date: LocalDate? = null,
        val bike: Bike? = null,
        val departures: DeparturesResponse? = null,
        val arrivals: ArrivalsResponse? = null,
        val stopover: StopoverResponse? = null,
        val ridingMinutes: Int? = null,
        val distanceMeters: Int? = null,
        val reduceAmountWon: Int? = null
    )

    data class DeparturesResponse(
        val longitude: String? = null,
        val latitude: String? = null,
        val detailAddress: String? = null
    )

    data class ArrivalsResponse(
        val longitude: String? = null,
        val latitude: String? = null,
        val detailAddress: String? = null
    )

    data class StopoverResponse(
        val longitude: String? = null,
        val latitude: String? = null,
        val detailAddress: String? = null
    )


    data class RidingListResponse(
        val currentPage: Int,
        val totalPages: Int,
        val totalItems: Long,
        val ridings: List<RidingResponse>
    )

    data class RidingDateListResponse(
            val ridings: List<RidingResponse>
    )


    //랭킹 조회
    @Operation(summary = "랭킹 조회", description = "")
    @Parameters(
            Parameter(name = "startDate", description = "조회 시작일"),
            Parameter(name = "endDate", description = "조회 마감일"),
            Parameter(name = "page", description = "페이지 수"),
            Parameter(name = "size", description = "페이징 사이즈")
    )
    @GetMapping("/rankings")
    fun getRanking(
            @Validated @RequestHeader("Bearer") token: String,
            @RequestParam("startDate") startDate: LocalDate,
            @RequestParam("endDate") endDate: LocalDate,
            @RequestParam("page") page: Int,
            @RequestParam("size") size: Int,
    ): ApiResponse<RankingListResponse> {

        jwtTokenProvider.getEmailFromJwt(token)

        val pageable: Pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "totalDistance"))

        // 랭킹 데이터 조회
        val rankings = ridingHistoryService.getRankingByDistance(startDate, endDate, pageable)

        // 전체 통계 조회
        val totalStatistics = ridingHistoryService.getTotalStatistics(startDate, endDate)

        // 랭킹 응답 리스트 생성
        val rankingResponseList = rankings.map { ranking ->
            RankingResponse(
                    _id = ranking._id,
//                    nickname = userNicknames[ranking.email] ?: "Unknown",
                    nickname = ranking._id?.let { userService.findUserByEmail(it)?.nickname },
                    totalDistanceMeters = ranking.totalDistanceMeters,
                    totalRidingMinutes = ranking.totalRidingMinutes,
                    co2Grams = 0.021 * ranking.totalDistanceMeters!!,
                    kcal = 0.01925 * ranking.totalDistanceMeters
            )
        }



        val rankingListResponse = RankingListResponse(
                currentPage = page + 1,
                totalPages = (totalStatistics.totalUsers / size) + 1,
                totalItems = totalStatistics.totalUsers.toLong(),
                userCount = rankingResponseList.size,
                totalRidingDistanceMeters = totalStatistics.totalDistanceMeters,
                totalCo2Grams = 0.021 * totalStatistics.totalDistanceMeters,
                totalRidingMinutes = totalStatistics.totalRidingMinutes,
                totalTransportationExpenses = totalStatistics.totalReduceAmountWon,
                rankingList = rankingResponseList
        )

        return ApiResponse.success(ResponseCode.READ_SUCCESS, rankingListResponse)

    }

    data class TotalStatistics(
            val totalDistanceMeters: Int,
            val totalRidingMinutes: Int,
            val totalUsers: Int,
            val totalReduceAmountWon: Int
    )

    data class RankingResponse(
            val _id: String? = null,
            val nickname: String? = null,
            val totalDistanceMeters: Int? = null,
            val totalRidingMinutes: Int? = null,
            val co2Grams: Double = 0.0,
            val kcal: Double = 0.0
    )


    data class RankingListResponse(
            val currentPage: Int,
            val totalPages: Int,
            val totalItems: Long,
            val userCount: Int,
            val totalRidingDistanceMeters: Int,
            val totalCo2Grams: Double,
            val totalRidingMinutes: Int,
            val totalTransportationExpenses: Int,
            val rankingList: List<RankingResponse>
    )
}