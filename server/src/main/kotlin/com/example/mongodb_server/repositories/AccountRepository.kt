package com.example.mongodb_server.repositories

import com.example.mongodb_server.repositories.entities.Account
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface AccountRepository: MongoRepository<Account, String> {
    fun findOneByAccountId(id: ObjectId): Account

    override fun deleteAll()
}