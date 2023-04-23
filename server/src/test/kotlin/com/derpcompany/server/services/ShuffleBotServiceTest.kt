package com.derpcompany.server.services

import dev.kord.cache.api.DataCache
import dev.kord.cache.api.Query
import dev.kord.core.ClientResources
import dev.kord.core.Kord
import dev.kord.core.cache.data.VoiceStateData
import dev.kord.core.entity.Member
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

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
    lateinit var channelService: DiscordChannelService

    @MockK
    lateinit var cache: DataCache

    val random = Random(1)

    val scope = TestScope()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher(scope.testScheduler))

        MockKAnnotations.init(this)
        service = ShuffleBotService(
            kord,
            random,
            scope,
            channelService,
        )

        every { clientResources.defaultStrategy } returns EntitySupplyStrategy.rest
        every { kord.defaultSupplier } returns defaultSupplier
        every { kord.resources } returns clientResources
        every { kord.cache } returns cache
        every { rest.interaction } returns interactionService
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // TESTS
    @Suppress("LongMethod")
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `call handleInteraction on audio channel`() = scope.runTest {
        // WHEN
        val interaction =
            spyk(GuildChatInputCommandInteraction(ShuffleBotData.defaultInteractionData, kord, defaultSupplier))
        val channel = spyk(
            ShuffleBotData.createTestChannel(
                kord = kord,
                supplier = defaultSupplier,
            )
        )

        coEvery { kord.getChannel(ShuffleBotData.defaultChannelId, any()) } returns channel
        coEvery { kord.getChannelOf<VoiceChannel>(ShuffleBotData.defaultChannelId) } returns channel

        val members = listOf(
            ShuffleBotData.createTestVoiceStateData(ShuffleBotData.defaultMemberId),
            ShuffleBotData.createTestVoiceStateData(ShuffleBotData.memberId2),
            ShuffleBotData.createTestVoiceStateData(ShuffleBotData.memberId3),
        )
        val query: Query<VoiceStateData> = mockk()
        every { query.asFlow() } returns members.asFlow()

        every { channel getProperty "voiceStates" } returns members.map { VoiceState(it, kord, defaultSupplier) }
            .asFlow()
        coEvery {
            defaultSupplier.getMember(
                any(),
                eq(ShuffleBotData.defaultMemberId)
            )
        } returns ShuffleBotData.createTestMember(
            ShuffleBotData.defaultMemberId,
            ShuffleBotData.defaultUsername,
            kord = kord,
            supplier = defaultSupplier,
        )
        coEvery {
            defaultSupplier.getMember(
                any(),
                eq(ShuffleBotData.memberId2)
            )
        } returns ShuffleBotData.createTestMember(
            ShuffleBotData.memberId2,
            "empathy",
            kord = kord,
            supplier = defaultSupplier,
        )
        coEvery {
            defaultSupplier.getMember(
                any(),
                eq(ShuffleBotData.memberId3)
            )
        } returns ShuffleBotData.createTestMember(
            ShuffleBotData.memberId3,
            "animus",
            kord = kord,
            supplier = defaultSupplier,
        )

        // DO
        val responseBuilder = service.handleInteraction(interaction)
        val response = InteractionResponseModifyBuilder().apply(responseBuilder)

        // ASSERT
        assertEquals("Group 1: animus\n" +
                "Group 2: empathy\n" +
                "Group 3: cramsan\n", response.content)
    }

    /**
     * Test our shuffle algorithm is creating groups based on input values with 7 members
     */
    @Test
    fun `shuffle list of 7 members and get two groups of 3 and 4 members`() {
        // WHEN
        val membersList = listOf<Member>(
            mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(),
        )
        val groupCount = 2
        val groupSize = 4

        // DO
        val groupList = service.shuffleUsers(membersList, groupCount)

        // ASSERT
        assertEquals(groupCount, groupList.size)
        assertEquals(groupSize, groupList[0].size)
        assertEquals(groupSize - 1, groupList[1].size)
    }

    /**
     * Test our shuffle algorithm is creating groups based on input values
     */
    @Test
    fun `shuffle list of 11 members and get 3 groups of 4, 4, and 3 members`() {
        // WHEN
        val membersList = listOf<Member>(
            mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(),
        )
        val groupCount = 3
        val groupSize = 4

        // DO
        val groupList = service.shuffleUsers(membersList, groupCount)

        // ASSERT
        assertEquals(groupCount, groupList.size)
        assertEquals(groupSize, groupList[0].size)
        assertEquals(groupSize, groupList[1].size)
        assertEquals(groupSize - 1, groupList[2].size)
    }

    @Test
    fun `shuffle list of 10 members into 3 groups`() {
        // WHEN
        val empathy = mockk<Member>()
        val cramsan = mockk<Member>()
        val zax = mockk<Member>()
        val abba = mockk<Member>()
        val holmes = mockk<Member>()
        val hyth = mockk<Member>()
        val moto = mockk<Member>()
        val bard = mockk<Member>()
        val adsf = mockk<Member>()
        val test = mockk<Member>()

        val membersList = listOf(
            empathy, cramsan, zax, abba,
            holmes, hyth, moto, bard,
            adsf, test,
        )
        val groupCount = 3

        // DO
        val groupList = service.shuffleUsers(membersList, groupCount)

        // ASSERT
        assertEquals(groupCount, groupList.size)
        assertEquals(4, groupList[0].size)
        assertEquals(3, groupList[1].size)
        assertEquals(3, groupList[2].size)
        assertEquals(listOf(cramsan, holmes, empathy, hyth), groupList[0])
        assertEquals(listOf(moto, zax, adsf), groupList[1])
        assertEquals(listOf(test, bard, abba), groupList[2])
    }

    @Test
    fun `shuffle list of 3 members into 4 groups`() {
        // WHEN
        val empathy = mockk<Member>()
        val cramsan = mockk<Member>()
        val zax = mockk<Member>()
        val membersList = listOf(
            empathy, cramsan, zax,
        )
        val groupCount = 4

        // DO
        val groupList = service.shuffleUsers(membersList, groupCount)

        // ASSERT
        assertEquals(3, groupList.size)
        assertEquals(1, groupList[0].size)
        assertEquals(1, groupList[1].size)
        assertEquals(1, groupList[2].size)
        assertEquals(zax, groupList[0].first())
        assertEquals(cramsan, groupList[1].first())
        assertEquals(empathy, groupList[2].first())
    }

    @Test
    fun `shuffle list of members into -1 groups should return empty`() {
        // WHEN
        val empathy = mockk<Member>()
        val cramsan = mockk<Member>()
        val zax = mockk<Member>()
        val abba = mockk<Member>()
        val membersList = listOf(
            empathy, cramsan, zax, abba,
        )
        val groupCount = -1

        // DO
        val groupList = service.shuffleUsers(membersList, groupCount)

        // ASSERT
        assertEquals(0, groupList.size)
    }

    @Test
    fun `shuffle list of members into 0 groups should return empty`() {
        // WHEN
        val empathy = mockk<Member>()
        val cramsan = mockk<Member>()
        val zax = mockk<Member>()
        val abba = mockk<Member>()
        val membersList = listOf(
            empathy, cramsan, zax, abba,
        )
        val groupCount = 0

        // DO
        val groupList = service.shuffleUsers(membersList, groupCount)

        // ASSERT
        assertEquals(0, groupList.size)
    }
}