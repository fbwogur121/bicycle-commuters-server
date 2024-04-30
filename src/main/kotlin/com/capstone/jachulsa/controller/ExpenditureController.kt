package com.capstone.jachulsa.controller

import com.capstone.jachulsa.domain.Expenditure
import com.capstone.jachulsa.service.ExpenditureService
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/expenditure")
class ExpenditureController(private val service: ExpenditureService) {

    // POST expenditure : 지출 생성
    @PostMapping
    fun createExpenditure(@RequestBody request: ExpenditureRequest): ExpenditureResponse {
        val expenditure = request.toExpenditure()
        val expenditureId = service.saveExpenditure(expenditure)
        return ExpenditureResponse(expenditureId.toString())
    }

    data class ExpenditureRequest(
        val userId: String,
        val expenditureType: String,
        val expenditureAmountWon: String,
        val date: LocalDate,
        val note: String?
    ) {
        fun toExpenditure(): Expenditure {
            return Expenditure(
                userId = this.userId,
                expenditureType = this.expenditureType,
                expenditureAmountWon = this.expenditureAmountWon,
                date = this.date,
                note = this.note
            )
        }
    }

    data class ExpenditureResponse(val expenditureId: String?)

    // GET /expenditure: 지출 기록 조회
    @GetMapping
    fun getExpenditure() : String{
        return "메롱"
    }
}








