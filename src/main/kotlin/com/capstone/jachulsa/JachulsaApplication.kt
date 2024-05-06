package com.capstone.jachulsa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class JachulsaApplication

fun main(args: Array<String>) {
	runApplication<JachulsaApplication>(*args)
}
//
//@Bean
//fun init(repository: UserRepository): CommandLineRunner {
//	return CommandLineRunner { args: Array<String?>? ->
//		val user = User(
//				user_id = "1",
//				email = "test@test.com",
//				name = "Test User",
//				nickname = "test",
//				sex = "M",
//				birthdate = LocalDate.now(),
//				is_active = true,
//				is_public = true,
//				address = Address(city = "Seoul", detail_address = "123-456"),
//				total_riding = TotalRiding(total_distance_meters = 1000, total_riding_minutes = 60, total_CO2_grams = 500, total_calories = 200)
//		)
//		repository.save(user)
//	}
//}