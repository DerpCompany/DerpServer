package com.derpcompany.server

import com.ninjasquad.springmockk.MockkBean
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/15/2022 11:26
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests(@Autowired val restTemplate: TestRestTemplate) {
    @MockkBean
    private lateinit var userRepository: UserRepository

    @BeforeAll
    fun setup() {
        println(">> Setup")
    }

    @Test
    fun `Assert login page title, content and status code`() {
        println(">> Assert login page title, content and status code")
        val entity = restTemplate.getForEntity<String>("/")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("<h1>Login</h1>")
    }

    @AfterAll
    fun teardown() {
        println(">> Tear down")
    }
}