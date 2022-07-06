package com.derpcompany.server.repositories

import com.derpcompany.server.repositories.entities.Account
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Repository class for querying within the Mongo DB for accounts
 */
@Repository
interface AccountRepository : MongoRepository<Account, String> {
    /**
     * Query for an account by account ID
     */
    fun findOneByAccountId(id: ObjectId): Account

    /**
     * Query for an account by username
     */
    fun findByUsername(username: String): Account

    /**
     * Query for all accounts with the same role
     */
    fun findByRole(role: String): List<Account>

    /**
     * override for deleting all accounts
     */
    override fun deleteAll()
}
