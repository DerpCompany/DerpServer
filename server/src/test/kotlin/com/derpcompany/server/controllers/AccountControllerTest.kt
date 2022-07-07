package com.derpcompany.server.controllers

import com.derpcompany.server.controllers.data.AccountResponse
import com.derpcompany.server.controllers.data.Roles
import com.derpcompany.server.network.models.AccountRequest
import com.derpcompany.server.repositories.ProfileRepository
import com.derpcompany.server.services.AccountService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/29/2022 17:18
 */

@WebMvcTest
internal class AccountControllerTest(@Autowired val mockMvc: MockMvc)  {

    @MockkBean
    lateinit var accountService: AccountService

    @MockkBean
    lateinit var profileRepository: ProfileRepository

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
    fun setUp() = Unit

    @AfterEach
    fun tearDown() = Unit

    // TESTS
    @Test
    fun `should return all accounts`() {
        // WHEN
        every { accountService.getAllAccounts() } returns
                ResponseEntity.ok(listOf(testAccount1, testAccount2, testAccount3))

        // DO
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/account")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // ASSERT
        val body: List<AccountResponse> = mapper.readValue(result.response.contentAsString)
        assertEquals(3, body.size)
    }

    @Test
    fun `should return single account by id`() {
        // WHEN
        every { accountService.getOneAccountById(testAccount1.accountId) } returns ResponseEntity.ok(testAccount1)

        // DO
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/account/${testAccount1.accountId}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // ASSERT
        val body: AccountResponse = mapper.readValue(result.response.contentAsString)
        assertEquals(testAccount1.accountId, body.accountId)
    }

    @Test
    fun `should return NOT FOUND for non-existent id`() {
        // WHEN
        every { accountService.getOneAccountById(testAccount1.accountId) } returns ResponseEntity(HttpStatus.NOT_FOUND)

        // DO
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/account/${testAccount1.accountId}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andReturn()

        // ASSERT
        assert(result.response.contentAsString.isEmpty())
    }

    @Test
    fun `should return single account by username`() {
        // WHEN
        every { accountService.getOneAccountByUsername(testAccount1.username) } returns ResponseEntity.ok(testAccount1)

        // DO
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/account/username/${testAccount1.username}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // ASSERT
        val body: AccountResponse = mapper.readValue(result.response.contentAsString)
        assertEquals(testAccount1.username, body.username)
    }

    @Test
    fun `should return NOT FOUND for non-existent username`() {
        // WHEN
        every { accountService.getOneAccountByUsername(testAccount1.username) } returns
                ResponseEntity(HttpStatus.NOT_FOUND)

        // DO
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/account/username/${testAccount1.username}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
            .andReturn()

        // ASSERT
        assert(result.response.contentAsString.isEmpty())
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/getByRolesTests.csv"], numLinesToSkip = 1)
    fun `should return a list of accounts with the same role`(role: String, index: Int) {
        // WHEN
        val accounts = listOf(testAccount1, testAccount2, testAccount3)
        every { accountService.getAccountsByRole(Roles.ADMIN) } returns ResponseEntity.ok(listOf(testAccount1))
        every { accountService.getAccountsByRole(Roles.MODERATOR) } returns ResponseEntity.ok(listOf(testAccount2))
        every { accountService.getAccountsByRole(Roles.MEMBER) } returns ResponseEntity.ok(listOf(testAccount3))

        // DO
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/account/role/${role}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // ASSERT
        val body: List<AccountResponse> = mapper.readValue(result.response.contentAsString)
        assertEquals(1, body.size)
        assertEquals(accounts[index].accountId, body.first().accountId)
    }

    @Test
    fun `should create a new user account`() {
        // WHEN
        val accountRequest = AccountRequest(testAccount2.accountId, testAccount2.email, "password")
        val accountRequestAsString = mapper.writeValueAsString(accountRequest)
        every { accountService.createAccount(accountRequest) } returns
                ResponseEntity(testAccount2, HttpStatus.CREATED)

        // DO
        val result = mockMvc.perform(MockMvcRequestBuilders.post("/api/account")
            .content(accountRequestAsString)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // ASSERT
        val body: AccountResponse = mapper.readValue(result.response.contentAsString)
        assertEquals(testAccount2.accountId, body.accountId)
        assertEquals(testAccount2.username, body.username)
        assertEquals(testAccount2.email, body.email)
    }

    @Test
    fun `should update an existing account `() {
        // WHEN
        val accountRequest = AccountRequest(testAccount2.accountId, testAccount2.email, "password2")
        val accountRequestAsString = mapper.writeValueAsString(accountRequest)
        every { accountService.updateAccount(accountRequest, testAccount2.accountId) } returns
                ResponseEntity.ok(testAccount2)

        // DO
        val result = mockMvc.perform(MockMvcRequestBuilders.put("/api/account/${testAccount2.accountId}")
            .content(accountRequestAsString)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()

        // ASSERT
        val body: AccountResponse = mapper.readValue(result.response.contentAsString)
        assertEquals(testAccount2.accountId, body.accountId)
        assertEquals(testAccount2.username, body.username)
        assertEquals(testAccount2.email, body.email)
    }

    @Test
    fun `should delete an existing account`() {
        // WHEN
        every { accountService.deleteAccount(testAccount2.accountId) } returns ResponseEntity.noContent().build()

        // DO
        val result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/account/${testAccount2.accountId}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
            .andReturn()

        // ASSERT
        assert(result.response.contentAsString.isEmpty())
    }

    companion object {
        val testAccount1 = AccountResponse(
            accountId = "123abc",
            username = "test1",
            email = "a@b.c",
            role = Roles.ADMIN,
            createdDate = 200,
            modifiedDate = 500,
        )
        val testAccount2 = AccountResponse(
            accountId = "456def",
            username = "test2",
            email = "b@b.c",
            role = Roles.MODERATOR,
            createdDate = 300,
            modifiedDate = 600,
        )
        val testAccount3 = AccountResponse(
            accountId = "789ghi",
            username = "test3",
            email = "c@b.c",
            role = Roles.MEMBER,
            createdDate = 400,
            modifiedDate = 700,
        )
    }
}