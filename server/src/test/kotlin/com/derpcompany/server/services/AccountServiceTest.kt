package com.derpcompany.server.services

import com.derpcompany.server.controllers.data.AccountResponse
import com.derpcompany.server.controllers.data.Roles
import com.derpcompany.server.repositories.AccountRepository
import com.derpcompany.server.repositories.ProfileRepository
import com.derpcompany.server.repositories.entities.Account
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

/**
 * Author: cramsan
 * Version: 1.0
 * Date: 7/7/2022 17:18
 */

internal class AccountServiceTest {

    @RelaxedMockK
    lateinit var accountRepository: AccountRepository

    @RelaxedMockK
    lateinit var profileRepository: ProfileRepository

    lateinit var service: AccountService

    val mapper = ObjectMapper().registerModule(
        KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false)
            .configure(KotlinFeature.NullIsSameAsDefault, false)
            .configure(KotlinFeature.SingletonSupport, false)
            .configure(KotlinFeature.StrictNullChecks, false)
            .build()
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = AccountService(accountRepository, profileRepository)
    }

    @AfterEach
    fun tearDown() = Unit

    // TESTS
    @Test
    fun `should return all accounts`() {
        // WHEN
        every { accountRepository.findAll() } returns listOf(testAccountEntity1, testAccountEntity2, testAccountEntity3)

        // DO
        val result = service.getAllAccounts()

        // ASSERT
        assertEquals(ResponseEntity.ok(listOf(testAccount1, testAccount2, testAccount3)), result)
    }

    companion object {
        val testAccountEntity1 = Account(
            accountId = ObjectId.get(),
            username = "test1",
            email = "a@b.c",
            role = Roles.ADMIN,
            password = "pass1",
            createdDate = LocalDateTime.now(),
            modifiedDate = LocalDateTime.now(),
        )
        val testAccountEntity2 = Account(
            accountId = ObjectId.get(),
            username = "test2",
            email = "b@b.c",
            role = Roles.MODERATOR,
            password = "pass2",
            createdDate = LocalDateTime.now(),
            modifiedDate = LocalDateTime.now(),
        )
        val testAccountEntity3 = Account(
            accountId = ObjectId.get(),
            username = "test3",
            email = "c@b.c",
            role = Roles.MEMBER,
            password = "pass3",
            createdDate = LocalDateTime.now(),
            modifiedDate = LocalDateTime.now(),
        )

        val testAccount1 = AccountResponse(
            accountId = testAccountEntity1.accountId.toHexString(),
            username = "test1",
            email = "a@b.c",
            role = Roles.ADMIN,
            createdDate = 0,
            modifiedDate = 0,
        )
        val testAccount2 = AccountResponse(
            accountId = testAccountEntity2.accountId.toHexString(),
            username = "test2",
            email = "b@b.c",
            role = Roles.MODERATOR,
            createdDate = 0,
            modifiedDate = 0,
        )
        val testAccount3 = AccountResponse(
            accountId = testAccountEntity3.accountId.toHexString(),
            username = "test3",
            email = "c@b.c",
            role = Roles.MEMBER,
            createdDate = 0,
            modifiedDate = 0,
        )
    }
}