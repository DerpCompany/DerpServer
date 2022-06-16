package com.derpcompany.server

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

    /**
     * When the article is clicked, we want to check the information is correct when redirected to new page
     */
    @Test
    fun `Assert article page title, content and status code`() {
        println(">> Assert article page title, content and status code")
        val title = "Reactor Aluminium has landed"
        val entity = restTemplate.getForEntity<String>("/article/${title.toSlug()}")

        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains(title, "Lorem ipsum", "dolor sit amet")
    }

    @AfterAll
    fun teardown() {
        println(">> Tear down")
    }
}