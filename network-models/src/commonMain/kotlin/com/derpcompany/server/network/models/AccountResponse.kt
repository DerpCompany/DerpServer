package com.derpcompany.server.network.models

/**
 * Dataset for the HTTP response
 */
data class AccountResponse(
    val accountId: String,
    val username: String,
    val email: String,
    val role: Roles,
    val createdDate: Long,
    val modifiedDate: Long,
)