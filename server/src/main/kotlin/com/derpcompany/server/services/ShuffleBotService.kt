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
) {

    /**
     * Handle an [interaction] of type [GuildChatInputCommandInteraction]. Return a lambda that will be used to create
     * a response.
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
                content = "ERROR MESSAGE"
            }
        }

        // Fetch the channel, for real this time.
        val voiceChannel = kord.getChannelOf<VoiceChannel>(id = channelId) ?: throw IllegalStateException(
            "Failed to fetch voice channel",
        )

        // Get the list of members in this channel
        val membersRaw = voiceChannel.voiceStates.toList()
        val members = membersRaw.map { it.getMember() }

        val usernames = members.joinToString("\n") { it.username }
        logger.debug("Members in channel ${channel.data.name.value}, to be split in $groupCount groups: [$usernames]")

        // Build the response
        return {
            content = usernames
        }
    }

    companion object {
        var logger: Logger = LoggerFactory.getLogger(ShuffleBotService::class.java)
    }
}
