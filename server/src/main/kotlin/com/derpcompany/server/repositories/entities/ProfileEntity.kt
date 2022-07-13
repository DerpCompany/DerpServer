package com.derpcompany.server.repositories.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/29/2022 14:27
 *
 * Representation on a profile in the repository layer.
 */

@Document("profile")
data class ProfileEntity(
    @Id
    val profileId: ObjectId,
    val username: String,
    val email: String,
    val role: Roles,
    // TODO: Add profileImg, Bio Description, etc
)
