package com.derpcompany.server.services

import com.derpcompany.server.Qualifiers
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import kotlinx.coroutines.flow.toList
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import kotlin.random.Random

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
) {

    /**
     * Handle an [interaction] of type [GuildChatInputCommandInteraction].
     * Return a lambda that will be used to create a response.
     */
    suspend fun handleInteraction(
        interaction: GuildChatInputCommandInteraction,
    ): InteractionResponseModifyBuilder.() -> Unit {
        val command = interaction.command
        val channelId: Snowflake = interaction.channelId

        // Get an argument passed in the command
        val groupCount = command.integers.getValue("groups")

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

        // Get the list of members in this channel
        val membersRaw = voiceChannel.voiceStates.toList()
        val members = membersRaw.map {
            it.getMember().username
        }

        // Check if there is at least one person in the VC
        if (membersRaw.isEmpty()) {
            return {
                content = "The VC is empty, \uD83D\uDE44 what are you trying to do?"
            }
        }

        val usernames = members.joinToString(", ")
        logger.debug("Members in channel ${channel.data.name.value}, to be split in $groupCount groups: [$usernames]")

        val shuffledMembers = shuffleUsers(members, groupCount.toInt())

        // Build the response
        return {
            content = printShuffledMembers(shuffledMembers)
        }
    }

    /**
     * Shuffles the [members] in the voice channel and groups them into the number of [groupCount]
     * Return a list of groups created and their respective list of users
     */
    fun shuffleUsers(
        members: List<String>,
        groupCount: Int,
    ): List<List<String>> {
        if (groupCount <= 0) {
            return emptyList()
        }

        val groupList: MutableList<MutableList<String>> = mutableListOf()

        // Shuffle the list of members and save in a new list
        val shuffledMembers = members.shuffled(random)

        // determine number of members in each group
        val totalMembers = shuffledMembers.size
        val groupSize = totalMembers / groupCount // already will floor the val when dividing two ints

        // create your groups and add them to a list
        val smallGroup: MutableList<String> = mutableListOf()
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
        shuffledMembers: List<List<String>>,
    ): String {
        val shuffleString = StringBuilder()

        shuffledMembers.forEachIndexed { index, members ->
            shuffleString.append("Group ${index + 1}: ")
            val membersString = members.joinToString(", ")
            shuffleString.append(membersString)
            shuffleString.append("\n")
        }
        return shuffleString.toString()
    }

    companion object {
        var logger: Logger = LoggerFactory.getLogger(ShuffleBotService::class.java)
    }
}
