package com.example.mongodb_server.repositories.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("user")
data class SavedUser (
    @Id
    val userId: ObjectId,
    val username: String,
    val email: String,
    val role: String, // TODO: convert to ENUM
    val createdDate: LocalDateTime,
    val modifiedDate: LocalDateTime,
)
