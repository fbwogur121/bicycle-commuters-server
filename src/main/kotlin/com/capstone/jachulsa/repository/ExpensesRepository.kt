package com.capstone.jachulsa.repository

import com.capstone.jachulsa.domain.Expenses
import org.springframework.data.mongodb.repository.MongoRepository

interface ExpensesRepository :MongoRepository<Expenses, String>{
}