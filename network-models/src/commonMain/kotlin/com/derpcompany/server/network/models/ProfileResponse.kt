package com.derpcompany.server.network.models

/**
 * Author: garci
 * Version: 1.0
 * Date: 7/6/2022 16:25
 *
 * Dataset for HTTP response for profiles
 */
data class ProfileResponse(
    val profileId: String,
    val username: String,
    val email: String,
    val role: Roles, // TODO: convert to ENUM
)