package com.derpcompany.server.integrationtests.helpers

import com.derpcompany.server.repositories.entities.AccountEntity
import com.derpcompany.server.repositories.entities.Roles
import org.bson.types.ObjectId

/**
 * @author cramsan
 */

val objectId1 = ObjectId.get()
val objectId2 = ObjectId.get()
val objectId3 = ObjectId.get()
val objectId4 = ObjectId.get()
val objectId5 = ObjectId.get()
val objectId6 = ObjectId.get()
val objectId7 = ObjectId.get()

val testAccount1 = AccountEntity(
    objectId1,
    "empathyawaits",
    "empathyawaits@gmail.com",
    Roles.ADMIN,
    "test1234",
    0,
    100,
)

val testAccount2 = AccountEntity(
    objectId2,
    "cramsan",
    "crams@gmail.com",
    Roles.MODERATOR,
    "test1234",
    0,
    100,
)

val testAccount3 = AccountEntity(
    objectId3,
    "hythloday",
    "hyth@gmail.com",
    Roles.ADMIN,
    "test1234",
    0,
    100,
)
val testAccount4 = AccountEntity(
    objectId4,
    "taco",
    "taco@gmail.com",
    Roles.MODERATOR,
    "test1234",
    0,
    100,
)
val testAccount5 = AccountEntity(
    objectId5,
    "animus",
    "animus@gmail.com",
    Roles.ADMIN,
    "test1234",
    0,
    100,
)
val testAccount6 = AccountEntity(
    objectId6,
    "jouhou",
    "houjou@gmail.com",
    Roles.ADMIN,
    "test1234",
    0,
    100,
)
val testAccount7 = AccountEntity(
    objectId7,
    "steely",
    "wools@gmail.com",
    Roles.MEMBER,
    "test1234",
    0,
    100,
)