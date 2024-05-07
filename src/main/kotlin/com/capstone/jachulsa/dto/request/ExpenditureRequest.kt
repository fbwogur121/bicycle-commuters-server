package com.capstone.jachulsa.dto.request

import com.capstone.jachulsa.domain.Expenditure
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class ExpenditureRequest(
    @field:NotBlank(message = "2001")
    val userId: String,
    @field:NotBlank(message = "2001")
    val expenditureType: String,
    @field:NotBlank(message = "2001")
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