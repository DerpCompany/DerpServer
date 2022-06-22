package com.example.mongodb_server.repositories

import com.example.mongodb_server.repositories.entities.SavedUser
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<SavedUser, String> {
    fun findOneByUserId(id: ObjectId): SavedUser

    fun findByUsername(username: String): SavedUser

    fun findByRole(role: String): List<SavedUser>

    override fun deleteAll()
}