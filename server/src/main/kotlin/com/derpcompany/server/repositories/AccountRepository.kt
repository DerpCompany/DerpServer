package com.derpcompany.server.repositories

import com.derpcompany.server.repositories.entities.AccountEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Repository class for querying within the Mongo DB for accounts
 */
@Repository
interface AccountRepository : MongoRepository<AccountEntity, String> {
    /**
     * Query for an account by account ID
     */
    fun findOneByAccountId(id: ObjectId): AccountEntity

    /**
     * Query for an account by username
     */
    fun findByUsername(username: String): AccountEntity

    /**
     * Query for all accounts with the same role
     */
    fun findByRole(role: String): List<AccountEntity>

    /**
     * override for deleting all accounts
     */
    override fun deleteAll()
}
