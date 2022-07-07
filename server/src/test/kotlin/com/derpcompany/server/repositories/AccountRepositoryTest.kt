package com.derpcompany.server.repositories

import com.derpcompany.server.controllers.data.Roles
import com.derpcompany.server.repositories.entities.Account
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import java.time.LocalDateTime

/**
 * Author: cramsan
 * Version: 1.0
 * Date: 7/7/2022 17:18
 */

@DataMongoTest
internal class AccountRepositoryTest @Autowired constructor(
    private val accountRepository: AccountRepository,
) {

    @BeforeEach
    fun setUp() {
        accountRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() = Unit

    // TESTS
    @Test
    fun `should return all accounts`() {
        // WHEN
        val expected = listOf(testAccount1, testAccount2, testAccount3)
        expected.forEach {
            accountRepository.insert(it)
        }

        // DO
        val result = accountRepository.findAll()

        // ASSERT
        assertEquals(3, expected.size)
        expected.forEachIndexed { index, account ->
            assertEquals(account.accountId, result[index].accountId)
            assertEquals(account.email, result[index].email)
            assertEquals(account.username, result[index].username)
            assertEquals(account.role, result[index].role)
        }
    }

    companion object {
        val testAccount1 = Account(
            accountId = ObjectId.get(),
            username = "test1",
            email = "a@b.c",
            role = Roles.ADMIN,
            password = "pass1",
            createdDate = LocalDateTime.now(),
            modifiedDate = LocalDateTime.now(),
        )
        val testAccount2 = Account(
            accountId = ObjectId.get(),
            username = "test2",
            email = "b@b.c",
            role = Roles.MODERATOR,
            password = "pass2",
            createdDate = LocalDateTime.now(),
            modifiedDate = LocalDateTime.now(),
        )
        val testAccount3 = Account(
            accountId = ObjectId.get(),
            username = "test3",
            email = "c@b.c",
            role = Roles.MEMBER,
            password = "pass3",
            createdDate = LocalDateTime.now(),
            modifiedDate = LocalDateTime.now(),
        )
    }
}