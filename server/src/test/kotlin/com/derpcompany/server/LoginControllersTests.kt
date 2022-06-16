package com.derpcompany.server

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/15/2022 16:32
 */

@WebMvcTest
class LoginControllersTests(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    private lateinit var userRepository: UserRepository

    @MockkBean
    private lateinit var articleRepository: ArticleRepository

    @Test
    fun `List articles`() {
        val empathyAwaits = User(
            "empathyAwaits", "garcia.alcia1990@gmail.com", "admin", "Discord"
        )
        val spring5Article = Article("Spring Framework 5.0 goes GA", "Dear Spring community ...", "Lorem ipsum", empathyAwaits)
        val spring43Article = Article("Spring Framework 4.3 goes GA", "Dear Spring community ...", "Lorem ipsum", empathyAwaits)

        every { articleRepository.findAllByOrderByAddedAtDesc() } returns listOf(spring5Article, spring43Article)

        mockMvc.perform(get("/api/article/").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].author.username").value(empathyAwaits.username))
            .andExpect(jsonPath("\$.[0].slug").value(spring5Article.slug))
            .andExpect(jsonPath("\$.[1].author.username").value(empathyAwaits.username))
            .andExpect(jsonPath("\$.[1].slug").value(spring43Article.slug))
    }

    @Test
    fun `List users`() {
        val empathyAwaits = User(
            "empathyAwaits", "garcia.alcia1990@gmail.com", "admin", "Discord"
        )
        val cramsan = User(
            "cramsan", "cramsan@gmail.com", "admin", "Discord"
        )

        every { userRepository.findAll() } returns listOf(empathyAwaits, cramsan)

        mockMvc.perform(get("/api/user/").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].username").value(empathyAwaits.username))
            .andExpect(jsonPath("\$.[1].username").value(cramsan.username))
    }
}