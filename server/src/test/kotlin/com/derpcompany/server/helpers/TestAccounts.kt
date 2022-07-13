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
    email = "a@b.c",
    role = RolesWireType.ADMIN,
    createdDate = 200,
    modifiedDate = 500,
)
val testAccountResponse2 = AccountResponse(
    accountId = "62ce572c5fa6d1270bd2ac44",
    username = "test2",
    email = "b@b.c",
    role = RolesWireType.MODERATOR,
    createdDate = 300,
    modifiedDate = 600,
)
val testAccountResponse3 = AccountResponse(
    accountId = "62ce572c5fa6d1270bd2ac45",
    username = "test3",
    email = "c@b.c",
    role = RolesWireType.MEMBER,
    createdDate = 400,
    modifiedDate = 700,
)

val testAccount1 = Account(
    accountId = "62ce572c5fa6d1270bd2ac43",
    username = "test1",
    email = "a@b.c",
    password = "pass1",
    role = RolesWireType.ADMIN,
    createdDate = Instant.ofEpochMilli(200),
    modifiedDate = Instant.ofEpochMilli(500),
)
val testAccount2 = Account(
    accountId = "62ce572c5fa6d1270bd2ac44",
    username = "test2",
    email = "b@b.c",
    password = "pass2",
    role = RolesWireType.MODERATOR,
    createdDate = Instant.ofEpochMilli(300),
    modifiedDate = Instant.ofEpochMilli(600),
)
val testAccount3 = Account(
    accountId = "62ce572c5fa6d1270bd2ac45",
    username = "test3",
    email = "c@b.c",
    password = "pass3",
    role = RolesWireType.MEMBER,
    createdDate = Instant.ofEpochMilli(400),
    modifiedDate = Instant.ofEpochMilli(700),
)

val testAccountRequest1 = AccountRequest(
    username = "test1",
    email = "a@b.c",
    password = "pass1",
)
val testAccountRequest2 = AccountRequest(
    username = "test2",
    email = "b@b.c",
    password = "pass2",
)
val testAccountRequest3 = AccountRequest(
    username = "test3",
    email = "c@b.c",
    password = "pass3",
)

val testAccountEntity1 = AccountEntity(
    accountId = ObjectId("62ce572c5fa6d1270bd2ac43"),
    username = "test1",
    email = "a@b.c",
    role = Roles.ADMIN,
    password = "pass1",
    createdDate = 200,
    modifiedDate = 500,
)
val testAccountEntity2 = AccountEntity(
    accountId = ObjectId("62ce572c5fa6d1270bd2ac44"),
    username = "test2",
    email = "b@b.c",
    role = Roles.MODERATOR,
    password = "pass2",
    createdDate = 300,
    modifiedDate = 600,
)
val testAccountEntity3 = AccountEntity(
    accountId = ObjectId("62ce572c5fa6d1270bd2ac45"),
    username = "test3",
    email = "c@b.c",
    role = Roles.MEMBER,
    password = "pass3",
    createdDate = 400,
    modifiedDate = 700,
)