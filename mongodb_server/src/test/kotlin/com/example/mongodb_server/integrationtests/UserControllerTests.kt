package com.example.mongodb_server.integrationtests

import com.example.mongodb_server.controllers.ResponseUser
import com.example.mongodb_server.entities.SavedUser
import com.example.mongodb_server.repositories.UserRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/22/2022 12:13
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTests @Autowired constructor(
    private val userRepository: UserRepository, private val restTemplate: TestRestTemplate
) {
    // SETUP
    private val testUser1 = SavedUser(ObjectId(), "empathyawaits", "empathyawaits@gmail.com", "admin")
    private val testUser2 = SavedUser(ObjectId(), "cramsan", "crams@gmail.com", "moderator")
    private val testUser3 = SavedUser(ObjectId(), "hythloday", "hyth@gmail.com", "admin")
    private val testUser4 = SavedUser(ObjectId(), "taco", "taco@gmail.com", "moderator")
    private val testUser5 = SavedUser(ObjectId(), "animus", "animus@gmail.com", "admin")
    private val testUser6 = SavedUser(ObjectId(), "jouhou", "houjou@gmail.com", "admin")
    private val testUser7 = SavedUser(ObjectId(), "steely", "wools@gmail.com", "member")

    @LocalServerPort
    protected var port: Int = 0

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
    }

    private fun getRootUrl(): String {
        return "http://localhost:$port/api/user"
    }

    private fun saveUsers() {
        userRepository.save(testUser1)
        userRepository.save(testUser2)
        userRepository.save(testUser3)
        userRepository.save(testUser4)
        userRepository.save(testUser5)
        userRepository.save(testUser6)
        userRepository.save(testUser7)
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
            ResponseUser::class.java
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
            ResponseUser::class.java
        )

        // ASSERT
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(username, response.body?.username)
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/getUserByRoleTests.csv"], numLinesToSkip = 1 )
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


}