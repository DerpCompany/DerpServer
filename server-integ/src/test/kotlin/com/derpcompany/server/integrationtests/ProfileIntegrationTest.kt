package com.derpcompany.server.integrationtests

import com.derpcompany.server.integrationtests.helpers.testProfile1
import com.derpcompany.server.integrationtests.helpers.testProfile2
import com.derpcompany.server.integrationtests.helpers.testProfile3
import com.derpcompany.server.integrationtests.helpers.testProfile4
import com.derpcompany.server.integrationtests.helpers.testProfile5
import com.derpcompany.server.integrationtests.helpers.testProfile6
import com.derpcompany.server.integrationtests.helpers.testProfile7
import com.derpcompany.server.network.wiretypes.ProfileResponse
import com.derpcompany.server.network.wiretypes.RolesWireType
import com.derpcompany.server.repositories.AccountRepository
import com.derpcompany.server.repositories.ProfileRepository
import com.derpcompany.server.repositories.entities.ProfileEntity
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
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/29/2022 14:45
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileIntegrationTest @Autowired constructor(
    private val profileRepository: ProfileRepository,
    private val accountRepository: AccountRepository,
    private val restTemplate: TestRestTemplate
) {
    // SETUP


    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        accountRepository.deleteAll()
        profileRepository.deleteAll()
    }

    private fun getRootUrl(): String {
        return "http://localhost:$port/api"
    }

    private fun saveProfile() {
        profileRepository.save(testProfile1)
        profileRepository.save(testProfile2)
        profileRepository.save(testProfile3)
        profileRepository.save(testProfile4)
        profileRepository.save(testProfile5)
        profileRepository.save(testProfile6)
        profileRepository.save(testProfile7)
    }

    // TESTS
    @Test
    fun `should return all profiles`() {
        // WHEN
        saveProfile()

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl() + "/profile",
            List::class.java
        )

        // ASSERT
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(7, response.body?.size)
    }

    @Test
    fun `should return single profile by id`() {
        // WHEN
        saveProfile()
        val id = testProfile7.profileId

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl() + "/profile/$id",
            ProfileResponse::class.java
        )

        // ASSERT
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(id.toHexString(), response.body?.profileId)
    }

    @Test
    fun `should return single profile by username`() {
        // WHEN
        saveProfile()
        val username = testProfile3.username

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl() + "/profile/username/$username",
            ProfileResponse::class.java
        )

        // ASSERT
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(username, response.body?.username)
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/getByRolesTests.csv"], numLinesToSkip = 1)
    fun `should return a list of profiles with the same role`(role: String, expected: Int) {
        // WHEN
        saveProfile()

        // DO
        val response = restTemplate.getForEntity(
            getRootUrl() + "/profile/role/$role",
            List::class.java
        )

        // ASSERT
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(expected, response.body?.size)
    }

    @Test
    fun `placeholderTest`() {
        // WHEN

        // DO

        // ASSERT
    }
}