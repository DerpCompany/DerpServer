package com.derpcompany.server.services

import dev.kord.cache.api.DataCache
import dev.kord.cache.api.Query
import dev.kord.core.ClientResources
import dev.kord.core.Kord
import dev.kord.core.cache.data.VoiceStateData
import dev.kord.core.entity.VoiceState
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.core.supplier.EntitySupplier
import dev.kord.core.supplier.EntitySupplyStrategy
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import dev.kord.rest.service.InteractionService
import dev.kord.rest.service.RestClient
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Author: cramsan
 * Version: 1.0
 * Date: 13/12/2022 17:18
 */
class ShuffleBotServiceTest {

    private lateinit var service: ShuffleBotService

    @MockK
    lateinit var kord: Kord

    @MockK
    lateinit var rest: RestClient

    @MockK
    lateinit var interactionService: InteractionService

    @MockK
    lateinit var defaultSupplier: EntitySupplier

    @MockK
    lateinit var clientResources: ClientResources

    @MockK
    lateinit var cache: DataCache

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        service = ShuffleBotService(kord)

        every { clientResources.defaultStrategy } returns EntitySupplyStrategy.rest
        every { kord.defaultSupplier } returns defaultSupplier
        every { kord.resources } returns clientResources
        every { kord.cache } returns cache
        every { rest.interaction } returns interactionService
    }

    @AfterEach
    fun tearDown() = Unit

    // TESTS
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `call handleInteraction on audio channel`() = runTest {
        // WHEN
        val interaction = spyk(GuildChatInputCommandInteraction(ShuffleBotData.defaultInteractionData, kord, defaultSupplier))
        val channel = spyk(ShuffleBotData.createTestChannel(
            kord = kord,
            supplier = defaultSupplier,
        ))

        coEvery { kord.getChannel(ShuffleBotData.defaultChannelId, any()) } returns channel
        coEvery { kord.getChannelOf<VoiceChannel>(ShuffleBotData.defaultChannelId) } returns channel

        val members = listOf(
            ShuffleBotData.createTestVoiceStateData(ShuffleBotData.defaultMemberId),
            ShuffleBotData.createTestVoiceStateData(ShuffleBotData.memberId2),
            ShuffleBotData.createTestVoiceStateData(ShuffleBotData.memberId3),
        )
        val query: Query<VoiceStateData> = mockk()
        every { query.asFlow() } returns members.asFlow()

        every { channel getProperty "voiceStates" } returns members.map { VoiceState(it, kord, defaultSupplier) }.asFlow()
        coEvery { defaultSupplier.getMember(any(), eq(ShuffleBotData.defaultMemberId)) } returns ShuffleBotData.createTestMember(
            ShuffleBotData.defaultMemberId,
            ShuffleBotData.defaultUsername,
            kord = kord,
            supplier = defaultSupplier,
        )
        coEvery { defaultSupplier.getMember(any(), eq(ShuffleBotData.memberId2)) } returns ShuffleBotData.createTestMember(
            ShuffleBotData.memberId2,
            "empathy",
            kord = kord,
            supplier = defaultSupplier,
        )
        coEvery { defaultSupplier.getMember(any(), eq(ShuffleBotData.memberId3)) } returns ShuffleBotData.createTestMember(
            ShuffleBotData.memberId3,
            "animus",
            kord = kord,
            supplier = defaultSupplier,
        )

        // DO
        val responseBuilder = service.handleInteraction(interaction)
        val response = InteractionResponseModifyBuilder().apply(responseBuilder)

        // ASSERT
        assertEquals("""
            cramsan
            empathy
            animus""".trimIndent(), response.content)
    }
}