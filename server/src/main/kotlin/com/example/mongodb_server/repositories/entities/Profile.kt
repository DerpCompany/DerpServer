package com.example.mongodb_server.repositories.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/29/2022 14:27
 */

@Document("profile")
data class Profile (
    @Id
    val profileId: ObjectId,
    val username: String,
    val email: String,
    val role: String,
    // TODO: Add profileImg, Bio Description, etc
)