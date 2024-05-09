package com.capstone.jachulsa.dto.request

import com.capstone.jachulsa.domain.Expenditure
import com.capstone.jachulsa.domain.enumtype.ExpenditureType
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class ExpenditureRequest(
    @field:NotBlank(message = "2001")
    val userId: String,
    val expenditureType: ExpenditureType,
    val expenditureAmountWon: Int,
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