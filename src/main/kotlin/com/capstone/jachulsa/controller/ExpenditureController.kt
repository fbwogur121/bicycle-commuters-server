package com.capstone.jachulsa.controller

import com.capstone.jachulsa.domain.enumtype.ExpenditureType
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import com.capstone.jachulsa.dto.ApiResponse
import com.capstone.jachulsa.dto.request.ExpenditureRequest
import com.capstone.jachulsa.dto.ResponseCode
import com.capstone.jachulsa.exception.CustomException
import com.capstone.jachulsa.service.ExpenditureService
import com.capstone.jachulsa.service.JwtTokenProvider
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Tag(name = "Expenditure API", description = "지출 API")
@RestController
@RequestMapping("/expenditure")
class ExpenditureController(private val service: ExpenditureService, private val jwtTokenProvider: JwtTokenProvider) {

    // POST expenditure : 지출 생성

    @Operation(summary = "지출 생성", description = "해당 유저의 지출 기록 생성")
    @PostMapping
    fun createExpenditure(@Validated @RequestBody request: ExpenditureRequest,
                          @RequestHeader("Authorization", required = true) authorizationHeader: String
    ): ApiResponse<ExpenditureResponse> {

        val email: String = jwtTokenProvider.getEmailFromJwt(authorizationHeader)

        val expenditure = service.createExpenditure(request.toExpenditure(email))
        val expenditureResponse = ExpenditureResponse(
                expenditureId = expenditure.expenditureId.toString(),
                email = expenditure.email,
                expenditureType = expenditure.expenditureType,
                expenditureAmountWon = expenditure.expenditureAmountWon,
                date = expenditure.date,
                note = expenditure.note
        )

        return ApiResponse.success(ResponseCode.CREATE_SUCCESS, expenditureResponse)
    }

    //GET /expenditure: 지출 기록 조회
    // 페이징 처리 필요, JWT 필요
    @GetMapping
    @Operation(summary = "지출 기록 조회", description = "지출 기록을 페이징 처리 후 반환, 페이지수는 0부터 !!")
    @Parameters(Parameter(name = "page", description = "페이지 수"),
                Parameter(name = "size", description = "페이징 사이즈"))
    fun getExpenditure(
            @RequestHeader("Authorization", required = true) authorizationHeader: String,
            @Validated @RequestParam("page",defaultValue = "0") page: Int,
            @Validated @RequestParam("size",defaultValue = "10") size: Int
    ): ApiResponse<ExpenditureListResponse?> {

        val email: String = jwtTokenProvider.getEmailFromJwt(authorizationHeader)

        val pageable: Pageable = PageRequest.of(page, size, Sort.by("date").descending())
        val expendituresPage = email?.let { service.getExpenditures(it, pageable) }
        val expenditures = expendituresPage?.content?.map { expenditure ->
            ExpenditureResponse(
                    expenditureId = expenditure.expenditureId.toString(),
                    email = expenditure.email,
                    expenditureType = expenditure.expenditureType,
                    expenditureAmountWon = expenditure.expenditureAmountWon,
                    date = expenditure.date,
                    note = expenditure.note
            )
        }
        val expenditureListResponse = expenditures?.let {
            ExpenditureListResponse(
                expenditures = it,
                currentPage = expendituresPage.number + 1,
                totalPages = expendituresPage.totalPages,
                totalItems = expendituresPage.totalElements
        )
        }

        return ApiResponse.success(ResponseCode.READ_SUCCESS, expenditureListResponse)
    }

    data class ExpenditureResponse(
        val expenditureId: String? = null,
        val email: String? = null,
        val expenditureType: ExpenditureType? = null,
        val expenditureAmountWon: Int? = null,
        val date: LocalDate? = null,
        val note: String? = null
    )

    data class ExpenditureListResponse(
        val currentPage: Int,
        val totalPages: Int,
        val totalItems: Long,
        val expenditures: List<ExpenditureResponse>
    )
}
