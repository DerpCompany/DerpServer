package com.derpcompany.server.repositories.entities

import com.derpcompany.server.controllers.data.Roles
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("account")
data class Account(
    @Id
    val accountId: ObjectId,
    val username: String,
    val email: String,
    val role: Roles,
    val password: String,
    val createdDate: LocalDateTime,
    val modifiedDate: LocalDateTime,
)
