package com.derpcompany.server.helpers

import com.derpcompany.server.network.wiretypes.ProfileResponse
import com.derpcompany.server.network.wiretypes.RolesWireType
import com.derpcompany.server.repositories.entities.ProfileEntity
import com.derpcompany.server.repositories.entities.Roles
import com.derpcompany.server.services.models.Profile
import org.bson.types.ObjectId

val testProfileResponse1 = ProfileResponse(
    profileId = "123abc",
    username = "test1",
    email = "a@b.c",
    role = RolesWireType.ADMIN,
)
val testProfileResponse2 = ProfileResponse(
    profileId = "456def",
    username = "test2",
    email = "b@b.c",
    role = RolesWireType.MODERATOR,
)
val testProfileResponse3 = ProfileResponse(
    profileId = "789ghi",
    username = "test3",
    email = "c@b.c",
    role = RolesWireType.MEMBER,
)

val testProfile1 = Profile(
    profileId = "123abc",
    username = "test1",
    email = "a@b.c",
    role = RolesWireType.ADMIN,
)
val testProfile2 = Profile(
    profileId = "456def",
    username = "test2",
    email = "b@b.c",
    role = RolesWireType.MODERATOR,
)
val testProfile3 = Profile(
    profileId = "789ghi",
    username = "test3",
    email = "c@b.c",
    role = RolesWireType.MEMBER,
)

val testProfileEntity1 = ProfileEntity(
    profileId = ObjectId.get(),
    username = "test1",
    email = "a@b.c",
    role = Roles.ADMIN,
)
val testProfileEntity2 = ProfileEntity(
    profileId = ObjectId.get(),
    username = "test2",
    email = "b@b.c",
    role = Roles.MODERATOR,
)
val testProfileEntity3 = ProfileEntity(
    profileId = ObjectId.get(),
    username = "test3",
    email = "c@b.c",
    role = Roles.MEMBER,
)