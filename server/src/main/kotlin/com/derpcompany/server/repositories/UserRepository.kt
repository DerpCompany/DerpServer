package com.derpcompany.server.repositories

import com.derpcompany.server.entities.*
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/15/2022 12:19
 */

interface UserRepository : MongoRepository<User, String> {
    fun findByUserId(Id: String): User?
}