package com.derpcompany.server.repositories.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Representation of an Account in the repository layer.
 *
 * @author cramsan
 */
@Document("account")
data class AccountEntity(
    @Id
    val accountId: ObjectId,
    val username: String,
    val email: String,
    val role: Roles,
    val password: String,
    val createdDate: Long,
    val modifiedDate: Long,
)
