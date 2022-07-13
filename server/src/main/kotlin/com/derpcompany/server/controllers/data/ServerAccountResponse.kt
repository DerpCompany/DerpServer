package com.derpcompany.server.controllers.data

import com.derpcompany.server.network.models.AccountResponse
import com.derpcompany.server.repositories.entities.Account

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
        modifiedDate = 0L,
    )
}
