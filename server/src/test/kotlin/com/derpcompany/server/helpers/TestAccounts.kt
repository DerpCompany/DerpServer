package com.derpcompany.server.helpers

import com.derpcompany.server.network.wiretypes.AccountRequest
import com.derpcompany.server.network.wiretypes.AccountResponse
import com.derpcompany.server.network.wiretypes.RolesWireType
import com.derpcompany.server.repositories.entities.AccountEntity
import com.derpcompany.server.repositories.entities.Roles
import com.derpcompany.server.services.models.Account
import org.bson.types.ObjectId
import java.time.Instant

val testAccountResponse1 = AccountResponse(
    accountId = "62ce572c5fa6d1270bd2ac43",
    username = "test1",
    email = "simpleemamil@gamil.com",
    role = RolesWireType.ADMIN,
    createdDate = 200,
    modifiedDate = 500,
)
val testAccountResponse2 = AccountResponse(
    accountId = "62ce572c5fa6d1270bd2ac44",
    username = "test2",
    email = "testemail@hotmail.com",
    role = RolesWireType.MODERATOR,
    createdDate = 300,
    modifiedDate = 600,
)
val testAccountResponse3 = AccountResponse(
    accountId = "62ce572c5fa6d1270bd2ac45",
    username = "test3",
    email = "test2email@me.com",
    role = RolesWireType.MEMBER,
    createdDate = 400,
    modifiedDate = 700,
)

val testAccount1 = Account(
    accountId = "62ce572c5fa6d1270bd2ac43",
    username = "test1",
    email = "simpleemamil@gamil.com",
    password = "Validp@ss1",
    role = RolesWireType.ADMIN,
    createdDate = Instant.ofEpochMilli(200),
    modifiedDate = Instant.ofEpochMilli(500),
)
val testAccount2 = Account(
    accountId = "62ce572c5fa6d1270bd2ac44",
    username = "test2",
    email = "testemail@hotmail.com",
    password = "v\$LidPa^s2",
    role = RolesWireType.MODERATOR,
    createdDate = Instant.ofEpochMilli(300),
    modifiedDate = Instant.ofEpochMilli(600),
)
val testAccount3 = Account(
    accountId = "62ce572c5fa6d1270bd2ac45",
    username = "test3",
    email = "test2email@me.com",
    password = "V3l!pa55*",
    role = RolesWireType.MEMBER,
    createdDate = Instant.ofEpochMilli(400),
    modifiedDate = Instant.ofEpochMilli(700),
)

val testAccountRequest1 = AccountRequest(
    username = "test1",
    email = "simpleemamil@gamil.com",
    password = "Validp@ss1",
)
val testAccountRequest2 = AccountRequest(
    username = "test2",
    email = "testemail@hotmail.com",
    password = "v\$LidPa^s2",
)
val testAccountRequest3 = AccountRequest(
    username = "test3",
    email = "test2email@me.com",
    password = "V3l!pa55*",
)

val testAccountEntity1 = AccountEntity(
    accountId = ObjectId("62ce572c5fa6d1270bd2ac43"),
    username = "test1",
    email = "simpleemamil@gamil.com",
    role = Roles.ADMIN,
    password = "Validp@ss1",
    createdDate = 200,
    modifiedDate = 500,
)
val testAccountEntity2 = AccountEntity(
    accountId = ObjectId("62ce572c5fa6d1270bd2ac44"),
    username = "test2",
    email = "testemail@hotmail.com",
    role = Roles.MODERATOR,
    password = "v\$LidPa^s2",
    createdDate = 300,
    modifiedDate = 600,
)
val testAccountEntity3 = AccountEntity(
    accountId = ObjectId("62ce572c5fa6d1270bd2ac45"),
    username = "test3",
    email = "test2email@me.com",
    role = Roles.MEMBER,
    password = "V3l!pa55*",
    createdDate = 400,
    modifiedDate = 700,
)