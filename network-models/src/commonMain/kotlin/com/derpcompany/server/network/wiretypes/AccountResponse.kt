package com.derpcompany.server.network.wiretypes

/**
 * Dataset for the HTTP response
 */
data class AccountResponse(
    val accountId: String,
    val username: String,
    val email: String,
    val role: RolesWireType,
    val createdDate: Long,
    val modifiedDate: Long,
)
