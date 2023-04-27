package com.derpcompany.server.services

import com.derpcompany.server.Qualifiers
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.value
import dev.kord.core.Kord
import dev.kord.core.behavior.edit
import dev.kord.core.entity.Member
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import dev.kord.rest.request.HttpStatus
import dev.kord.rest.request.KtorRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

/**
 * Author: cramsan
 * Version: 1.0
 * Date: 12/6/2022 11:25
 *
 * Service that controls the interactions for the ShuffleBot.
 */

@Service
class ShuffleBotService(
    @Qualifier(Qualifiers.SHUFFLE_BOT)
    private val kord: Kord,
    private val random: Random,
    private val scope: CoroutineScope,
    private val discordChannelService: DiscordChannelService,
) {

    /**
     * Handle an [interaction] of type [GuildChatInputCommandInteraction].
     * Return a lambda that will be used to create a response.
     */
    suspend fun handleInteraction(
        interaction: GuildChatInputCommandInteraction,
        groupCount: Long,
        moveToChannels: Boolean,
    ): InteractionResponseModifyBuilder.() -> Unit {
        val command = interaction.command
        val channelId: Snowflake = interaction.channelId
        val guildId: Snowflake = interaction.guildId

        // Fetch the channel to ensure we are on an audio channel
        val channel = kord.getChannel(id = channelId) ?: throw IllegalStateException("Failed to fetch channel")
        if (channel.type != ChannelType.GuildVoice) {
            return {
                content = "ONLY AVAILABLE IN VOICE CHANNELS \uD83E\uDDD0"
            }
        }

        // Fetch the channel, for real this time.
        val voiceChannel = kord.getChannelOf<VoiceChannel>(id = channelId) ?: throw IllegalStateException(
            "Failed to fetch voice channel",
        )
        val parentId: Snowflake? = voiceChannel.data.parentId?.value

        // Get the list of members in this channel
        val membersRaw = voiceChannel.voiceStates.toList().map {
            it.getMember()
        }
        val members = membersRaw.map {
            it.username
        }

        // Check if there is at least one person in the VC
        if (membersRaw.isEmpty()) {
            return {
                content = "The VC is empty, \uD83D\uDE44 what are you trying to do?"
            }
        }

        val usernames = members.joinToString(", ")
        logger.debug("Members in channel ${channel.data.name.value}, to be split in $groupCount groups: [$usernames]")

        val shuffledMembers = shuffleUsers(membersRaw, groupCount.toInt())

        if (moveToChannels) {
            moveUsersToTempChannelsAsync(
                shuffledMembers,
                guildId = guildId,
                parentId = parentId,
                responseChannelId = voiceChannel.id,
            )
        }

        // Build the response
        return {
            content = printShuffledMembers(shuffledMembers, moveToChannels)
        }
    }

    private fun moveUsersToTempChannelsAsync(
        shuffledMembers: List<List<Member>>,
        guildId: Snowflake,
        parentId: Snowflake?,
        responseChannelId: Snowflake,
    ) {
        logger.info("Creating temp ${shuffledMembers.size} temp folders for server $guildId and parentId $parentId")
        scope.launch {
            delay(5.seconds)

            try {
                shuffledMembers.forEach { group ->
                    val tempChannel = discordChannelService.createTempVoiceChannel(
                        guildId = guildId,
                        parentId = parentId,
                    )

                    group.forEach { member ->
                        member.edit {
                            voiceChannelId = tempChannel.id
                        }
                    }
                }
            } catch (exception: KtorRequestException) {
                val errorMessage = when (exception.status.toKtorStatusCode()) {
                    HttpStatusCode.Forbidden -> """
                        There was an error creating a channel. Please verify I have all permissions.
                    """.trimIndent()
                    else -> "Something went wrong. Please report this to the admins."
                }

                val channel = kord.getChannelOf<MessageChannel>(id = responseChannelId)
                channel?.createMessage(errorMessage)
            }
        }
    }

    /**
     * Shuffles the [members] in the voice channel and groups them into the number of [groupCount]
     * Return a list of groups created and their respective list of users
     */
    fun shuffleUsers(
        members: List<Member>,
        groupCount: Int,
    ): List<List<Member>> {
        if (groupCount <= 0) {
            return emptyList()
        }

        val groupList: MutableList<MutableList<Member>> = mutableListOf()

        // Shuffle the list of members and save in a new list
        val shuffledMembers = members.shuffled(random)

        // determine number of members in each group
        val totalMembers = shuffledMembers.size
        val groupSize = totalMembers / groupCount // already will floor the val when dividing two ints

        // create your groups and add them to a list
        val smallGroup: MutableList<Member> = mutableListOf()
        shuffledMembers.forEachIndexed { _, member ->
            smallGroup.add(member)
            if (smallGroup.size == groupSize) {
                groupList.add(smallGroup.toMutableList()) // copy the temp list into our groupList
                smallGroup.clear()
            }
        }

        // Distribute the remainder members across the existing lists
        smallGroup.forEachIndexed { index, member ->
            if (groupList.getOrNull(index) == null) {
                groupList.add(index, mutableListOf())
            }

            groupList[index].add(member)
        }

        return groupList
    }

    /**
     * Builds a string of the groups and their members to easily display the final
     * results of the shuffle
     */
    fun printShuffledMembers(
        shuffledMembers: List<List<Member>>,
        moveToChannels: Boolean,
    ): String {
        if (shuffledMembers.isEmpty()) {
            return "Could not create any group."
        }

        val shuffleString = StringBuilder()

        if (moveToChannels) {
            shuffleString.append("Users will be automatically moved.\n\n")
        }

        shuffledMembers.forEachIndexed { index, members ->
            val memberUsernames = members.map { it.username }

            shuffleString.append("Group ${index + 1}: ")
            val membersString = memberUsernames.joinToString(", ")
            shuffleString.append(membersString)
            shuffleString.append("\n")
        }
        return shuffleString.toString()
    }

    companion object {
        var logger: Logger = LoggerFactory.getLogger(ShuffleBotService::class.java)
    }
}

private fun HttpStatus.toKtorStatusCode(): HttpStatusCode = HttpStatusCode(
    value = code,
    description = message,
)
