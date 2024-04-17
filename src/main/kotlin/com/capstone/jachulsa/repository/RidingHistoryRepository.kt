package com.capstone.jachulsa.repository

import com.capstone.jachulsa.collection.RidingHistory
import org.springframework.data.mongodb.repository.MongoRepository

interface RidingHistoryRepository :MongoRepository<RidingHistory, String>{
}