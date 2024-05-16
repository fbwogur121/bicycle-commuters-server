package com.capstone.jachulsa

import com.capstone.jachulsa.domain.Address
import com.capstone.jachulsa.domain.TotalRiding
import com.capstone.jachulsa.domain.User
import com.capstone.jachulsa.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class JachulsaApplication

fun main(args: Array<String>) {
	runApplication<JachulsaApplication>(*args)
}
@Bean
fun init(repository: UserRepository): CommandLineRunner {
	return CommandLineRunner { args: Array<String?>? ->
		val user = User(
			nickname = "test",
			age = "23",
			gender = "M",
			email = "test@test.com",
			name = "Test User",
			birthday = "12-13",
			birthyear = "2001",
			profileImage = "http://profileImageTest.jpg",
			is_active = true,
			is_public = true,
			address = Address(city = "Seoul", detail_address = "123-456"),
			total_riding = TotalRiding(total_distance_meters = 1000, total_riding_minutes = 60, total_CO2_grams = 500, total_calories = 200)
		)
		repository.save(user)
	}
}