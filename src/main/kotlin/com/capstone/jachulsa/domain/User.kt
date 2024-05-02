//package com.capstone.jachulsa.domain
//
//import org.springframework.data.annotation.Id
//import org.springframework.data.mongodb.core.mapping.Document
//import java.time.LocalDate
//
//@Document(collection = "User")
//data class User(
//        @Id
//        val user_id: String,
//        val email: String,
//        val name: String,
//        val nickname: String,
//        val sex: String,
//        val birthdate: LocalDate,
//        val is_active: Boolean,
//        val is_public: Boolean?,
//        val address: Address?,
//        val total_riding: TotalRiding?
//)
//
//data class Address(
//        val city: String?,
//        val detail_address: String?
//)
//
//data class TotalRiding(
//        val total_distance_meters: Int?,
//        val total_riding_minutes: Int?,
//        val total_CO2_grams: Int?,
//        val total_calories: Int?
//)
package com.capstone.jachulsa.domain

import com.capstone.jachulsa.domain.common.BaseEntity
import com.capstone.jachulsa.domain.enums.Gender
import com.capstone.jachulsa.domain.enums.SocialType
import com.capstone.jachulsa.domain.enums.MemberStatus
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "User")
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(nullable = false, length = 20)
        var name: String? = null,

        @Column(nullable = false, length = 40)
        var address: String? = null,

        @Column(nullable = false, length = 40)
        var specAddress: String? = null,

        @Enumerated(EnumType.STRING)
        @Column(columnDefinition = "VARCHAR(10)")
        var gender: Gender? = null,

        @Enumerated(EnumType.STRING)
        var socialType: SocialType? = null,

        @Enumerated(EnumType.STRING)
        @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
        var status: MemberStatus? = null,

        var inactiveDate: LocalDate? = null,

        @Column(nullable = false, length = 50)
        var email: String? = null,

        @Column(columnDefinition = "INT DEFAULT 0")
        var point: Int = 0
) : BaseEntity()
