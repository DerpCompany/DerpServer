package com.derpcompany.server.services.models

import com.derpcompany.server.network.wiretypes.RolesWireType
import java.time.Instant

/**
 * Representation of an Account in the domain layer.
 *
 * @author cramsan
 */
data class Account(
    val accountId: String,
    val username: String,
    val email: String,
    val role: RolesWireType,
    val password: String,
    val createdDate: Instant,
    val modifiedDate: Instant,
)
