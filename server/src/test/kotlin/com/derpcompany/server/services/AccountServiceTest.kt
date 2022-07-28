package com.derpcompany.server.services

import com.derpcompany.server.network.wiretypes.RolesWireType
import com.derpcompany.server.helpers.testAccount1
import com.derpcompany.server.helpers.testAccount2
import com.derpcompany.server.helpers.testAccount3
import com.derpcompany.server.helpers.testAccountEntity1
import com.derpcompany.server.helpers.testAccountEntity2
import com.derpcompany.server.helpers.testAccountEntity3
import com.derpcompany.server.helpers.testAccountRequest2
import com.derpcompany.server.helpers.testAccountRequest3
import com.derpcompany.server.helpers.testProfileEntity2
import com.derpcompany.server.helpers.testProfileEntity3
import com.derpcompany.server.repositories.AccountRepository
import com.derpcompany.server.repositories.ProfileRepository
import com.derpcompany.server.repositories.entities.Roles
import com.derpcompany.server.services.data.toAccount
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

/**
 * Author: cramsan
 * Version: 1.0
 * Date: 7/7/2022 17:18
 */

internal class AccountServiceTest {

    @MockK
    lateinit var accountRepository: AccountRepository

    @MockK
    lateinit var profileRepository: ProfileRepository

    lateinit var service: AccountService

    lateinit var clock: Clock

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())
        service = AccountService(accountRepository, profileRepository, clock)
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
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(listOf(testAccount1, testAccount2, testAccount3), result.body)
    }

    @Test
    fun `should return single account by id`() {
        // WHEN
        every { accountRepository.findOneByAccountId(testAccountEntity1.accountId) } returns testAccountEntity1

        // DO
        val result = service.getOneAccountById(testAccount1.accountId)

        // ASSERT
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(testAccount1, result.body)
    }

    @Test
    fun `should return NOT FOUND for non-existent id`() {
        // WHEN
        every { accountRepository.findOneByAccountId(testAccountEntity1.accountId) } throws EmptyResultDataAccessException(1)

        // DO
        val result = service.getOneAccountById(testAccount1.accountId)

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertFalse(result.hasBody())
    }

    @Test
    fun `should return single account by username`() {
        // WHEN
        every { accountRepository.findByUsername(testAccount1.username) } returns testAccountEntity1

        // DO
        val result = service.getOneAccountByUsername(testAccount1.username)

        // ASSERT
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(testAccount1, result.body)
    }

    @Test
    fun `should return NOT FOUND for non-existent username`() {
        // WHEN
        every { accountRepository.findByUsername(testAccount1.username) } throws EmptyResultDataAccessException(1)

        // DO
        val result = service.getOneAccountByUsername(testAccount1.username)

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertFalse(result.hasBody())
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/getByRolesTests.csv"], numLinesToSkip = 1)
    fun `should return a list of accounts with the same role`(role: String, index: Int) {
        // WHEN
        val accounts = listOf(
            testAccount1,
            testAccount2,
            testAccount3
        )
        every { accountRepository.findByRole(RolesWireType.ADMIN.name) } returns listOf(testAccountEntity1)
        every { accountRepository.findByRole(RolesWireType.MODERATOR.name) } returns listOf(testAccountEntity2)
        every { accountRepository.findByRole(RolesWireType.MEMBER.name) } returns listOf(testAccountEntity3)

        // DO
        val result = service.getAccountsByRole(Roles.valueOf(role))

        // ASSERT
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(1, result.body?.size)
        assertEquals(accounts[index].accountId, result.body?.first()?.accountId)
    }

    @Test
    fun `should create a new user account`() {
        // WHEN - whatever is passed is returned, since we're creating a new account
        every { accountRepository.save(any()) } returns testAccountEntity2
        every { profileRepository.save(any()) } returns testProfileEntity2

        // DO
        val result = service.createAccount(
            username = testAccountRequest2.username,
            email = testAccountRequest2.email,
            password = testAccountRequest2.password,
        )

        // ASSERT
        assertEquals(HttpStatus.CREATED, result.statusCode)
    }

    @Test
    fun `should update an existing account `() {
        // WHEN
        val updatedPassword = "updat3dPas&"
        val accountId = testAccount3.accountId

        val updatedAccountEntity = testAccountEntity3.copy(
            password = updatedPassword,
            modifiedDate = clock.millis(),
        )
        val updatedPasswordRequest = testAccountRequest3.copy(password = updatedPassword)

        // Mock the repository actions
        every { accountRepository.save(updatedAccountEntity) } returns updatedAccountEntity
        every { profileRepository.save(testProfileEntity3) } returns testProfileEntity3

        every { accountRepository.findOneByAccountId(ObjectId(accountId)) } returns testAccountEntity3
        every { profileRepository.findOneByProfileId(ObjectId(accountId)) } returns testProfileEntity3

        // DO
        val result = service.updateAccount(
            username = updatedPasswordRequest.username,
            email = updatedPasswordRequest.email,
            password = updatedPasswordRequest.password,
            id = testAccount3.accountId,
        )

        // ASSERT
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(updatedAccountEntity.toAccount(), result.body)
    }

    @Test
    fun `should delete an existing account`() {
        // WHEN
        every { accountRepository.deleteById(testAccount2.accountId) } returns Unit
        every { profileRepository.deleteById(testAccount2.accountId) } returns Unit

        // DO
        val result = service.deleteAccount(testAccount2.accountId)

        // ASSERT
        assertEquals(HttpStatus.NO_CONTENT, result.statusCode)
        assertFalse(result.hasBody())
    }
}