package com.derpcompany.server.integrationtests.helpers

import com.derpcompany.server.repositories.entities.ProfileEntity
import com.derpcompany.server.repositories.entities.Roles
import org.bson.types.ObjectId

/**
 * @author cramsan
 */
val testProfile1 = ProfileEntity(
    objectId1,
    "empathyawaits",
    "empathyawaits@gmail.com",
    Roles.ADMIN,
)
val testProfile2 = ProfileEntity(
    objectId2,
    "cramsan",
    "crams@gmail.com",
    Roles.MODERATOR,
)
val testProfile3 = ProfileEntity(
    objectId3,
    "hythloday",
    "hyth@gmail.com",
    Roles.ADMIN,
)
val testProfile4 = ProfileEntity(
    objectId4,
    "taco",
    "taco@gmail.com",
    Roles.MODERATOR,
)
val testProfile5 = ProfileEntity(
    objectId5,
    "animus",
    "animus@gmail.com",
    Roles.ADMIN,
)
val testProfile6 = ProfileEntity(
    objectId6,
    "jouhou",
    "houjou@gmail.com",
    Roles.ADMIN,
)
val testProfile7 = ProfileEntity(
    objectId7,
    "steely",
    "wools@gmail.com",
    Roles.MEMBER,
)