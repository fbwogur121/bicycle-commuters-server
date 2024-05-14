package com.capstone.jachulsa.domain

import org.springframework.cglib.core.Local
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import org.springframework.data.annotation.Id
@Document(collection = "User")
data class User(
        @Id
        val id: String? = null,
        val nickname: String?,
        val age: String?,
        val gender: String?,
        val email: String?,
        val name: String?,
        val birthday: String?,
        val birthyear: String?,
        val is_active: Boolean? = true,
        val is_public: Boolean? = true,
        val address: Address?,
        val total_riding: TotalRiding?
)

data class Address(
        val city: String?,
        val detail_address: String?
)

data class TotalRiding(
        val total_distance_meters: Int?,
        val total_riding_minutes: Int?,
        val total_CO2_grams: Int?,
        val total_calories: Int?
)