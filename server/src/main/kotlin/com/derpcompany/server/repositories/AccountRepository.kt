package com.derpcompany.server.repositories

import com.derpcompany.server.repositories.entities.Account
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface AccountRepository : MongoRepository<Account, String> {
    fun findOneByAccountId(id: ObjectId): Account

    fun findByUsername(username: String): Account

    fun findByRole(role: String): List<Account>

    override fun deleteAll()
}
