package com.derpcompany.server.services.data

import com.derpcompany.server.controllers.data.toRolesWireType
import com.derpcompany.server.repositories.entities.AccountEntity
import com.derpcompany.server.repositories.entities.ProfileEntity
import com.derpcompany.server.services.models.Account
import com.derpcompany.server.services.models.Profile
import java.time.Instant

/**
 * Shows the account details excluding sensitive data like password
 * TODO: Verify is we want to exclude email
 */
fun AccountEntity.toAccount(): Account {
    return Account(
        accountId = accountId.toHexString(),
        username,
        email,
        role.toRolesWireType(),
        password,
        createdDate = Instant.ofEpochMilli(createdDate),
        modifiedDate = Instant.ofEpochMilli(modifiedDate),
    )
}

/**
 * Shows the profile details
 * TODO: Verify is we want to exclude email
 */
fun ProfileEntity.toProfile(): Profile {
    return Profile(
        profileId = profileId.toHexString(),
        username,
        email,
        role.toRolesWireType(),
    )
}
