package com.derpcompany.server.controllers.data

import com.derpcompany.server.network.models.ProfileResponse
import com.derpcompany.server.repositories.entities.Profile


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