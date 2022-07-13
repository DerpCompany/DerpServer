package com.derpcompany.server.network.wiretypes

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
    val role: RolesWireType,
)