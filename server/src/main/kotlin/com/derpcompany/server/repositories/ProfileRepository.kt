package com.derpcompany.server.repositories

import com.derpcompany.server.repositories.entities.ProfileEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Repository class for querying within the Mongo DB for profiles
 */
@Repository
interface ProfileRepository : MongoRepository<ProfileEntity, String> {
    /**
     * Query for a profile by profile ID
     */
    fun findOneByProfileId(id: ObjectId): ProfileEntity

    /**
     * Query for a profile by username
     */
    fun findByUsername(username: String): ProfileEntity

    /**
     * Query all profiles with the same role
     */
    fun findByRole(role: String): List<ProfileEntity>

    /**
     * Override for deleting all profiles
     */
    override fun deleteAll()
}
