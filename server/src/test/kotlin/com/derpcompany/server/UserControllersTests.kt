package com.derpcompany.server

import com.derpcompany.server.entities.User
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
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
class UserControllersTests(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    private lateinit var userRepository: UserRepository

    @Test
    fun `List users`() {
        val empathyAwaits = User(
            "empathyAwaits", "garcia.alcia1990@gmail.com", "admin"
        )
        val cramsan = User(
            "cramsan", "cramsan@gmail.com", "admin"
        )

        every { userRepository.findAll() } returns listOf(empathyAwaits, cramsan)

        mockMvc.perform(get("/api/user/").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].username").value(empathyAwaits.username))
            .andExpect(jsonPath("\$.[1].username").value(cramsan.username))
    }
}