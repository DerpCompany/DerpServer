package com.example.mongodb_server.repositories

import com.example.mongodb_server.entities.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String> {
    fun findOneById(id: ObjectId): User

    fun findByUsername(username: String): User

    fun findByRole(role: String): List<User>

    override fun deleteAll()
}