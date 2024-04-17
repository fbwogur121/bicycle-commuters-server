package com.capstone.jachulsa.repository

import com.capstone.jachulsa.domain.RidingHistory
import org.springframework.data.mongodb.repository.MongoRepository

interface RidingHistoryRepository :MongoRepository<RidingHistory, String>{
}