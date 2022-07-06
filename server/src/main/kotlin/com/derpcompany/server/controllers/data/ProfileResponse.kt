package com.derpcompany.server.controllers.data

import com.derpcompany.server.repositories.entities.Profile

data class ProfileResponse(
    val profileId: String,
    val username: String,
    val email: String,
    val role: Roles, // TODO: convert to ENUM
)

/**
 * Shows the profile details
 * TODO: Verify is we want to exclude email
 */
fun Profile.toProfileResponse(): ProfileResponse {
    return ProfileResponse(
        profileId = profileId.toHexString(),
        username,
        email,
        role,
    )
}
