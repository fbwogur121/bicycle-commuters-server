package com.capstone.jachulsa.collection

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "Expenses")
data class Expenses(
        @Id
        val expenses_id: String,
        val user_id: String,
        val expenditure_type: String,
        val expenditure_amount_won: String,
        val date: LocalDate,
        val note: String?
)