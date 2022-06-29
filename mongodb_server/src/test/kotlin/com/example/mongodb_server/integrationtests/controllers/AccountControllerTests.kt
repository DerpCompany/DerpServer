package com.example.mongodb_server.integrationtests.controllers

import com.example.mongodb_server.controllers.data.AccountRequest
import com.example.mongodb_server.controllers.data.AccountResponse
import com.example.mongodb_server.controllers.data.ProfileResponse
import com.example.mongodb_server.repositories.entities.Account
import com.example.mongodb_server.repositories.AccountRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
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
class AccountControllerTests @Autowired constructor(
    private val accountRepository: AccountRepository, private val restTemplate: TestRestTemplate
) {
    // SETUP
    private val testAccount1 = Account(
        ObjectId(), "empathyawaits", "empathyawaits@gmail.com", "admin", "test1234", LocalDateTime
            .now(), LocalDateTime.now()
    )
    private val testAccount2 = Account(
        ObjectId(), "cramsan", "crams@gmail.com", "moderator", "test1234", LocalDateTime
            .now(), LocalDateTime.now()
    )

    @LocalServerPort
    protected var port: Int = 0

    @BeforeEach
    fun setUp() {
        accountRepository.deleteAll()
    }

    private fun getRootUrl(): String {
        return "http://localhost:$port/api"
    }

    private fun saveAccounts() {
        accountRepository.save(testAccount1)
        accountRepository.save(testAccount2)
    }

    @Test
    fun `should create a new user account`() {
        // WHEN
        val username = "steffybug"
        val email = "steffybug@gmail.com"
        val password = "test1234"

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