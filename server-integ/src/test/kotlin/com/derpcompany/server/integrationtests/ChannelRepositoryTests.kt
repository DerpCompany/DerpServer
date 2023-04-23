package com.derpcompany.server.integrationtests

import com.derpcompany.server.repositories.ChannelRepository
import com.derpcompany.server.repositories.entities.DiscordChannelEntity
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Snowflake
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Author: cramsan
 * Version: 1.0
 * Date: 4/22/2023 12:13
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChannelRepositoryTests @Autowired constructor(
    private val channelRepository: ChannelRepository,
) {
    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        channelRepository.deleteAll()
    }

    // TESTS
    @Test
    fun `write and then read from the database`() {
        val entity = DiscordChannelEntity(
            id = ObjectId.get(),
            channelId = Snowflake.min.value.toString(),
            guildId = Snowflake.max.value.toString(),
            type = ChannelType.GuildVoice.value,
            name = "Test Account",
            parentId = Snowflake.min.value.toString(),
        )

        channelRepository.insert(entity)

        val readEntity = channelRepository.findOneByChannelId(entity.channelId)

        assertEquals(entity, readEntity)
    }
}