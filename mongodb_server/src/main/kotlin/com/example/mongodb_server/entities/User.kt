package com.example.mongodb_server.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("userentity")
data class User (
    @Id
    val userID: ObjectId = ObjectId.get(),
    val username: String,
    val email: String,
    val role: String, // TODO: convert to ENUM
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val modifiedDate: LocalDateTime = LocalDateTime.now(),
)

@Document("linkedaccounts")
data class LinkedAccounts(
    @Id
    var userId: User,
    var accounts: Map<String, String> // list of SSOIDs from Oauth2 linked accounts with their keys(names)
)