package com.capstone.jachulsa.domain

import com.capstone.jachulsa.domain.enumtype.ExpenditureType
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "Expenditure")
data class Expenditure(
        @Id
        val expenditureId: ObjectId? = null,
        val userId: String,
        val expenditureType: ExpenditureType,
        val expenditureAmountWon: Int,
        val date: LocalDate,
        val note: String?
)