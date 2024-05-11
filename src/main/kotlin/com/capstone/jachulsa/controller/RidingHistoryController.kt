package com.capstone.jachulsa.controller

import com.capstone.jachulsa.domain.enumtype.Bike
import com.capstone.jachulsa.domain.enumtype.RidingType
import com.capstone.jachulsa.dto.ApiResponse
import com.capstone.jachulsa.dto.ResponseCode
import com.capstone.jachulsa.dto.request.RidingHistoryRequest
import com.capstone.jachulsa.service.RidingHistoryService
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
class RidingHistoryController (private val service: RidingHistoryService){

    // POST /riding : 라이딩 생성
    @Operation(summary = "라이딩 생성", description = "해당 유저의 라이딩기록을 생성. POST 요청 성공 시 ridingId 반환")
    @PostMapping("/riding")
    fun createRidingHistory(@Validated @RequestBody request: RidingHistoryRequest): ApiResponse<RidingResponse> {
        val ridingHistoryId = service.createRidingHistory(request.toRidingHistory())
        return ApiResponse.success(ResponseCode.CREATE_SUCCESS, RidingResponse(ridingHistoryId.toString()))
    }


    //라이딩 리스트 조회
    @Operation(summary = "라이딩 기록 조회", description = "조회 기간 내 모든 라이딩 기록을 페이징 처리 후 반환. userId != null && myRidesOnly = true일 시 기간 내 해당 유저의 라이딩 기록만 조회")
    @Parameters(
        Parameter(name = "startDate", description = "조회 시작일"),
        Parameter(name = "endDate", description = "조회 마감일"),
        Parameter(name = "myRidesOnly", description = "내 라이딩만 조회"),
        Parameter(name = "page", description = "페이지 수"),
        Parameter(name = "size", description = "페이징 사이즈")
    )
    @GetMapping("/ridings")
    fun getRidings(
        @RequestParam("startDate") startDate: LocalDate,
        @RequestParam("endDate") endDate: LocalDate,
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int,
        @RequestParam("myRidesOnly", defaultValue = "false") myRidesOnly: Boolean,
        @PathVariable(required = false) userId: String?
    ): ApiResponse<RidingListResponse> {
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("date").descending())

        val ridingsPage = service.getRidings(userId, startDate.minusDays(1), endDate.plusDays(1), myRidesOnly, pageable)

        val ridings = ridingsPage.content.map { riding ->
            RidingResponse(
                ridingId = riding.ridingHistoryId.toString(),
                userId = riding.userId,
                type = riding.type,
                date = riding.date,
                bike = riding.bike,
                departures = DeparturesResponse(
                    longitude = riding.departures.longitude,
                    latitude = riding.departures.latitude,
                    detailAddress = riding.departures.detailAddress
                ),
                arrivals = ArrivalsResponse(
                    longitude = riding.arrivals.longitude,
                    latitude = riding.arrivals.latitude,
                    detailAddress = riding.arrivals.detailAddress
                ),
                stopover = riding.stopover?.let {
                    StopoverResponse(
                        longitude = it.longitude,
                        latitude = it.latitude,
                        detailAddress = it.detailAddress
                    )
                },
                ridingMinutes = riding.ridingMinutes,
                distanceMeters = riding.distanceMeters,
                reduceAmountWon = riding.reduceAmountWon
            )
        }

        val ridingListResponse = RidingListResponse(
            currentPage = ridingsPage.number + 1, // Page number is zero-based
            totalPages = ridingsPage.totalPages,
            totalItems = ridingsPage.totalElements,
            ridings = ridings
        )

        return ApiResponse.success(ResponseCode.READ_SUCCESS, ridingListResponse)
    }



    //랭킹 조회
//    @Operation(summary = "랭킹 조회", description = "")
//    @Parameters(
//            Parameter(name = "startDate", description = "조회 시작일"),
//            Parameter(name = "endDate", description = "조회 마감일"),
//            Parameter(name = "myRidesOnly", description = "내 라이딩만 조회"),
//            Parameter(name = "page", description = "페이지 수"),
//            Parameter(name = "size", description = "페이징 사이즈")
//    )
//    @GetMapping("/ridings/{userId}")
//    fun getRanking(
//            @RequestParam("startDate") startDate: LocalDate,
//            @RequestParam("endDate") endDate: LocalDate,
//            @RequestParam("page") page: Int,
//            @RequestParam("size") size: Int,
//            @RequestParam("myRidesOnly", defaultValue = "false") myRidesOnly: Boolean,
//            @PathVariable(required = false) userId: String?
//    ): ApiResponse<RidingListResponse> {
//
//        return ApiResponse.success(ResponseCode.READ_SUCCESS, RankingListResponse)
//    }




//    //라이딩 상세조회
//    @Operation(summary = "라이딩 상세 조회", description = "")
//    @GetMapping("/riding/{ridingId}")
//    fun getRidingHistory(@PathVariable ridingId: String): ApiResponse<RidingResponse> {
//        val ridingHistory = service.getRidingHistoryById(ridingId)
//
//        val departuresResponse = ridingHistory?.departures?.let { departures ->
//            DeparturesResponse(
//                longitude = departures.longitude,
//                latitude = departures.latitude,
//                detailAddress = departures.detailAddress
//            )
//        }
//
//        val arrivalsResponse = ridingHistory?.arrivals?.let { arrivals ->
//            ArrivalsResponse(
//                longitude = arrivals.longitude,
//                latitude = arrivals.latitude,
//                detailAddress = arrivals.detailAddress
//            )
//        }
//
//        val stopoverResponse = ridingHistory?.stopover?.let { stopover ->
//            StopoverResponse(
//                longitude = stopover.longitude,
//                latitude = stopover.latitude,
//                detailAddress = stopover.detailAddress
//            )
//        }
//
//        val ridingResponse = RidingResponse(
//            ridingId = ridingHistory?.ridingHistoryId.toString(),
//            userId = ridingHistory?.userId,
//            type = ridingHistory?.type,
//            date = ridingHistory?.date,
//            bike = ridingHistory?.bike,
//            departures = departuresResponse,
//            arrivals = arrivalsResponse,
//            stopover = stopoverResponse,
//            ridingMinutes = ridingHistory?.ridingMinutes,
//            distanceMeters = ridingHistory?.distanceMeters,
//            reduceAmountWon = ridingHistory?.reduceAmountWon
//        )
//
//        return ApiResponse.success(ResponseCode.READ_SUCCESS, ridingResponse)
//    }




    data class RidingResponse(
        val ridingId: String? = null,
        val userId: String? = null,
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

    data class RankingResponse(
            val userId: String,
            val userName: String,
            val distanceMeters: Int,
            val ridingMinutes: Int,
            val co2Grams: Int,
            val kcal: Int
    )

    data class RankingListResponse(
            val currentPage: Int,
            val totalPages: Int,
            val totalItems: Long,
            val userCount: Int,
            val totalRidingDistanceMeters: Int,
            val totalTransportationExpenses: Int,
            val totalCo2Grams: Int,
            val totalRidingMinutes: Int,
            val rankingList: List<RankingResponse>
    )

}