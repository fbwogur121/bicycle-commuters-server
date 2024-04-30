package com.capstone.jachulsa.repository

import com.capstone.jachulsa.domain.Expenditure
import org.springframework.data.mongodb.repository.MongoRepository

interface ExpenditureRepository :MongoRepository<Expenditure, String>{
}