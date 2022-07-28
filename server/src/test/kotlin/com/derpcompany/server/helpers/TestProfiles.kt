package com.derpcompany.server.helpers

import com.derpcompany.server.network.wiretypes.ProfileResponse
import com.derpcompany.server.network.wiretypes.RolesWireType
import com.derpcompany.server.repositories.entities.ProfileEntity
import com.derpcompany.server.repositories.entities.Roles
import com.derpcompany.server.services.models.Profile
import org.bson.types.ObjectId

val testProfileResponse1 = ProfileResponse(
    profileId = "62ce572c5fa6d1270bd2ac43",
    username = "test1",
    email = "simpleemamil@gamil.com",
    role = RolesWireType.ADMIN,
)
val testProfileResponse2 = ProfileResponse(
    profileId = "62ce572c5fa6d1270bd2ac44",
    username = "test2",
    email = "testemail@hotmail.com",
    role = RolesWireType.MODERATOR,
)
val testProfileResponse3 = ProfileResponse(
    profileId = "62ce572c5fa6d1270bd2ac45",
    username = "test3",
    email = "test2email@me.com",
    role = RolesWireType.MEMBER,
)

val testProfile1 = Profile(
    profileId = "62ce572c5fa6d1270bd2ac43",
    username = "test1",
    email = "simpleemamil@gamil.com",
    role = RolesWireType.ADMIN,
)
val testProfile2 = Profile(
    profileId = "62ce572c5fa6d1270bd2ac44",
    username = "test2",
    email = "testemail@hotmail.com",
    role = RolesWireType.MODERATOR,
)
val testProfile3 = Profile(
    profileId = "62ce572c5fa6d1270bd2ac45",
    username = "test3",
    email = "test2email@me.com",
    role = RolesWireType.MEMBER,
)

val testProfileEntity1 = ProfileEntity(
    profileId = ObjectId("62ce572c5fa6d1270bd2ac43"),
    username = "test1",
    email = "simpleemamil@gamil.com",
    role = Roles.ADMIN,
)
val testProfileEntity2 = ProfileEntity(
    profileId = ObjectId("62ce572c5fa6d1270bd2ac44"),
    username = "test2",
    email = "testemail@hotmail.com",
    role = Roles.MODERATOR,
)
val testProfileEntity3 = ProfileEntity(
    profileId = ObjectId("62ce572c5fa6d1270bd2ac45"),
    username = "test3",
    email = "test2email@me.com",
    role = Roles.MEMBER,
)