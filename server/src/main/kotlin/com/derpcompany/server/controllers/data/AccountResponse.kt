package com.derpcompany.server.controllers.data

import com.derpcompany.server.repositories.entities.Account

data class AccountResponse(
    val accountId: String,
    val username: String,
    val email: String,
    val role: String, // TODO: convert to ENUM
    val createdDate: Long,
    val modifiedDate: Long,
)

/**
 * Shows the account details excluding sensitive data like password
 * TODO: Verify is we want to exclude email
 */
fun Account.toAccountResponse(): AccountResponse {
    return AccountResponse(
        accountId = accountId.toHexString(),
        username,
        email,
        role,
        createdDate = 0L,
        modifiedDate = 0L
    )
}