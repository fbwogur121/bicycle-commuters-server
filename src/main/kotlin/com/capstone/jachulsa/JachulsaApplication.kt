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
import java.time.LocalDate

@SpringBootApplication
@EnableMongoRepositories
class JachulsaApplication

fun main(args: Array<String>) {
	runApplication<JachulsaApplication>(*args)
}