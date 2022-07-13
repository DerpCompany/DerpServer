package com.derpcompany.server.integrationtests

import com.derpcompany.server.integrationtests.helpers.testAccount1
import com.derpcompany.server.integrationtests.helpers.testAccount2
import com.derpcompany.server.integrationtests.helpers.testAccount3
import com.derpcompany.server.integrationtests.helpers.testAccount4
import com.derpcompany.server.integrationtests.helpers.testAccount5
import com.derpcompany.server.integrationtests.helpers.testAccount6
import com.derpcompany.server.integrationtests.helpers.testAccount7
import com.derpcompany.server.integrationtests.helpers.testProfile1
import com.derpcompany.server.integrationtests.helpers.testProfile2
import com.derpcompany.server.network.wiretypes.AccountResponse
import com.derpcompany.server.network.wiretypes.RolesWireType
import com.derpcompany.server.network.wiretypes.AccountRequest
import com.derpcompany.server.repositories.entities.AccountEntity
import com.derpcompany.server.repositories.AccountRepository
import com.derpcompany.server.repositories.ProfileRepository
import com.derpcompany.server.repositories.entities.ProfileEntity
import com.derpcompany.server.repositories.entities.Roles
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/22/2022 12:13
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountIntegrationTests @Autowired constructor(
    private val accountRepository: AccountRepository,
    private val profileRepository: ProfileRepository,
    private val restTemplate: TestRestTemplate,
) {
    // SETUP - create the accounts and their respective profiles
    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        accountRepository.deleteAll()
    }

    private fun getRootUrl(): String {
        return "http://localhost:$port/api"
    }

    private fun saveAccounts() {
        accountRepository.save(testAccount1)
        profileRepository.save(testProfile1)
        accountRepository.save(testAccount2)
        profileRepository.save(testProfile2)
        accountRepository.save(testAccount3)
        accountRepository.save(testAccount4)
        accountRepository.save(testAccount5)
        accountRepository.save(testAccount6)
        accountRepository.save(testAccount7)
    }

    // TESTS
    @Test
    fun `should return all accounts`() {
        // WHEN
        saveAccounts()

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl() + "/account",
            List::class.java
        )

        // ASSERT
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(7, response.body?.size)
    }

    @Test
    fun `should return single account by id`() {
        // WHEN
        saveAccounts()
        val id = testAccount7.accountId

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl() + "/account/$id",
            AccountResponse::class.java
        )

        // ASSERT
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(id.toHexString(), response.body?.accountId)
    }

    @Test
    fun `should return NOT FOUND for non-existent id`() {
        // WHEN
        saveAccounts()
        val id = "62c5e04b123bb76da0731e32"

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl() + "/account/$id",
            AccountResponse::class.java
        )

        // ASSERT
        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `should return single account by username`() {
        // WHEN
        saveAccounts()
        val username = testAccount3.username

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl() + "/account/username/$username",
            AccountResponse::class.java
        )

        // ASSERT
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(username, response.body?.username)
    }

    @Test
    fun `should return NOT FOUND for non-existent username`() {
        // WHEN
        saveAccounts()
        val username = "userDoesNotExist"

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl() + "/account/username/$username",
            AccountResponse::class.java
        )

        // ASSERT
        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/getByRolesTests.csv"], numLinesToSkip = 1)
    fun `should return a list of accounts with the same role`(role: String, expected: Int) {
        // WHEN
        saveAccounts()

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl() + "/account/role/$role",
            List::class.java
        )

        // ASSERT
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(expected, response.body?.size)
    }

    @Test
    fun `should create a new user account`() {
        // WHEN
        val username = "steffybug"
        val email = "steffybug@gmail.com"
        val password = "tEst!1234"

        // DO
        val response = restTemplate.postForEntity(
            getRootUrl() + "/account",
            AccountRequest(username, email, password),
            AccountResponse::class.java
        )

        // ASSERT
        assertEquals(201, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(username, response.body?.username)
        assertEquals(email, response.body?.email)
    }

    @Test
    fun `should update an existing account `() {
        // WHEN
        saveAccounts()

        val userToUpdate = testAccount2.accountId
        val newUsername = "crams"
        val newEmail = "crasson@gmail.com"
        val password = "test4321"

        // DO
        val response = restTemplate.exchange(
            getRootUrl() + "/account/$userToUpdate",
            HttpMethod.PUT,
            HttpEntity(
                AccountRequest(newUsername, newEmail, password),
                HttpHeaders(),
            ),
            AccountResponse::class.java
        )

        // ASSERT
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(newUsername, response.body?.username)
        assertEquals(newEmail, response.body?.email)
    }

    @Test
    fun `should delete an existing account`() {
        // WHEN
        saveAccounts()
        val userToDelete = testAccount1.accountId


        // DO
        val deleteUser = restTemplate.exchange(
            getRootUrl() + "/account/$userToDelete",
            HttpMethod.DELETE,
            HttpEntity(null, HttpHeaders()),
            ResponseEntity::class.java
        )

        // ASSERT
        assertEquals(204, deleteUser.statusCode.value())
        assertThrows(EmptyResultDataAccessException::class.java) {
            accountRepository.findOneByAccountId(userToDelete)
        }
    }

    @Test
    fun `placeholderTest`() {
        // WHEN

        // DO

        // ASSERT
    }
}