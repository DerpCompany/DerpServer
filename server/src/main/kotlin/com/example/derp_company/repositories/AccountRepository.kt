package com.example.derp_company.repositories

import com.example.derp_company.repositories.entities.Account
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface AccountRepository: MongoRepository<Account, String> {
    fun findOneByAccountId(id: ObjectId): Account

    override fun deleteAll()
}