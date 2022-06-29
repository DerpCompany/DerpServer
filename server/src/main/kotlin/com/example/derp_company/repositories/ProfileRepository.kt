package com.example.derp_company.repositories

import com.example.derp_company.repositories.entities.Profile
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ProfileRepository: MongoRepository<Profile, String> {
    fun findOneByProfileId(id: ObjectId): Profile

    fun findByUsername(username: String): Profile

    fun findByRole(role: String): List<Profile>

    override fun deleteAll()
}