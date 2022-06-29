package com.example.mongodb_server.integrationtests

import com.example.mongodb_server.controllers.data.NewAccount
import com.example.mongodb_server.controllers.data.ProfileResponse
import com.example.mongodb_server.repositories.entities.Account
import com.example.mongodb_server.repositories.AccountRepository
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
class UserControllerTests @Autowired constructor(
    private val accountRepository: AccountRepository, private val restTemplate: TestRestTemplate
) {
    // SETUP
    private val testUser1 = Account(
        ObjectId(), "empathyawaits", "empathyawaits@gmail.com", "admin", LocalDateTime
            .now(), LocalDateTime.now()
    )
    private val testUser2 = Account(
        ObjectId(), "cramsan", "crams@gmail.com", "moderator", LocalDateTime
            .now(), LocalDateTime.now()
    )
    private val testUser3 = Account(
        ObjectId(), "hythloday", "hyth@gmail.com", "admin", LocalDateTime
            .now(), LocalDateTime.now()
    )
    private val testUser4 = Account(
        ObjectId(), "taco", "taco@gmail.com", "moderator", LocalDateTime
            .now(), LocalDateTime.now()
    )
    private val testUser5 = Account(
        ObjectId(), "animus", "animus@gmail.com", "admin", LocalDateTime
            .now(), LocalDateTime.now()
    )
    private val testUser6 = Account(
        ObjectId(), "jouhou", "houjou@gmail.com", "admin", LocalDateTime
            .now(), LocalDateTime.now()
    )
    private val testUser7 = Account(
        ObjectId(), "steely", "wools@gmail.com", "member", LocalDateTime
            .now(), LocalDateTime.now()
    )

    @LocalServerPort
    protected var port: Int = 0

    @BeforeEach
    fun setUp() {
        accountRepository.deleteAll()
    }

    private fun getRootUrl(): String {
        return "http://localhost:$port/api/user"
    }

    private fun saveUsers() {
        accountRepository.save(testUser1)
        accountRepository.save(testUser2)
        accountRepository.save(testUser3)
        accountRepository.save(testUser4)
        accountRepository.save(testUser5)
        accountRepository.save(testUser6)
        accountRepository.save(testUser7)
    }

    // TESTS
    @Test
    fun `should return all users`() {
        // WHEN
        saveUsers()

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl(),
            List::class.java
        )

        // ASSERT
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(7, response.body?.size)
    }

    @Test
    fun `should return single user by id`() {
        // WHEN
        saveUsers()
        val userId = testUser7.userId

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl() + "/id/$userId",
            ProfileResponse::class.java
        )

        // ASSERT
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(userId.toHexString(), response.body?.userId)
    }

    @Test
    fun `should return single user by username`() {
        // WHEN
        saveUsers()
        val username = testUser3.username

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl() + "/username/$username",
            ProfileResponse::class.java
        )

        // ASSERT
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(username, response.body?.username)
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/getUserByRoleTests.csv"], numLinesToSkip = 1)
    fun `should return a list of users with the same role`(role: String, expected: Int) {
        // WHEN
        saveUsers()

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl() + "/roles/$role",
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

        // DO
        val response = restTemplate.postForEntity(
            getRootUrl(),
            NewAccount(username, email),
            ProfileResponse::class.java
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
        saveUsers()

        val userToUpdate = testUser2.userId
        val newUsername = "crams"
        val newEmail = "crasson@gmail.com"

        // DO
        val response = restTemplate.exchange(
            getRootUrl() + "/$userToUpdate",
            HttpMethod.PUT,
            HttpEntity(
                NewAccount(newUsername, newEmail),
                HttpHeaders(),
            ),
            ProfileResponse::class.java
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
        saveUsers()
        val userToDelete = testUser4.userId


        // DO
        val deleteUser = restTemplate.exchange(
            getRootUrl() + "/$userToDelete",
            HttpMethod.DELETE,
            HttpEntity(null, HttpHeaders()),
            ResponseEntity::class.java
        )

        // ASSERT
        assertEquals(204, deleteUser.statusCode.value())
        assertThrows(EmptyResultDataAccessException::class.java) {
            accountRepository.findOneByUserId(userToDelete)
        }
    }

    @Test
    fun `placeholderTest`() {
        // WHEN

        // DO

        // ASSERT
    }
}