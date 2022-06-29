package com.example.derp_company.controllers.data

import com.example.derp_company.repositories.entities.Profile

data class ProfileResponse(
    val profileId: String,
    val username: String,
    val email: String,
    val role: String, // TODO: convert to ENUM
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