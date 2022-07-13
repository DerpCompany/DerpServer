package com.derpcompany.server.services.models

import com.derpcompany.server.network.wiretypes.RolesWireType

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/29/2022 14:27
 *
 * Representation on a profile in the domain layer.
 */

data class Profile(
    val profileId: String,
    val username: String,
    val email: String,
    val role: RolesWireType,
    // TODO: Add profileImg, Bio Description, etc
)
