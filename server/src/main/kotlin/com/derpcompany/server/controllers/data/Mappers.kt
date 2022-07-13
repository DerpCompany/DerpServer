package com.derpcompany.server.controllers.data

import com.derpcompany.server.network.wiretypes.AccountListResponse
import com.derpcompany.server.network.wiretypes.AccountResponse
import com.derpcompany.server.network.wiretypes.ProfileResponse
import com.derpcompany.server.network.wiretypes.RolesWireType
import com.derpcompany.server.repositories.entities.Roles
import com.derpcompany.server.services.models.Account
import com.derpcompany.server.services.models.Profile
import org.springframework.http.ResponseEntity

/**
 * Shows the account details excluding sensitive data like password
 * TODO: Verify is we want to exclude email
 */
fun Account.toAccountResponse(): AccountResponse {
    return AccountResponse(
        accountId = accountId,
        username,
        email,
        role,
        createdDate = 0L,
        modifiedDate = 0L,
    )
}

/**
 * Shows the profile details
 * TODO: Verify is we want to exclude email
 */
fun Profile.toProfileResponse(): ProfileResponse {
    return ProfileResponse(
        profileId = profileId,
        username,
        email,
        role,
    )
}

/**
 * Converts from the internal [Roles] enum to the public API [RolesWireType].
 */
fun Roles.toRolesWireType(): RolesWireType {
    return RolesWireType.valueOf(this.name)
}

/**
 * Converts from the public [RolesWireType] into the internal [Roles] enum.
 */
fun RolesWireType.toRoles(): Roles {
    return Roles.valueOf(this.name)
}

/**
 * Helper function to map the [ResponseEntity.body] from one type [M] to a type [R].
 */
fun <M, R>ResponseEntity<M>.mapBody(map: (body: M?) -> R): ResponseEntity<R> {
    val mappedBody = map(this.body)
    return ResponseEntity(mappedBody, this.headers, this.statusCode)
}

/**
 * Helper function to convert from an [accountList] into an [AccountListResponse].
 */
fun convertAccountListToResponse(accountList: List<Account>?): AccountListResponse? {
    return accountList?.map { it.toAccountResponse() }
}
