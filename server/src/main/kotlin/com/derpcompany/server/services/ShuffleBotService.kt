package com.derpcompany.server.services

import com.derpcompany.server.Qualifiers
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
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

    suspend fun onGuildChatInputCommandInteractionCreateEvent(event: GuildChatInputCommandInteractionCreateEvent) {
        val deferredResponse = event.interaction.deferPublicResponse()
        val command = event.interaction.command
        val channelId: Snowflake = event.interaction.channelId

        val groupCount = command.integers.getValue("groups")

        val channel = kord.getChannel(id = channelId) ?: throw IllegalStateException("Failed to fetch channel")
        if (channel.type != ChannelType.GuildVoice) {
            deferredResponse.respond {
                content = "ERROR MESSAGE"
            }
            return
        }

        val voiceChannel = kord.getChannelOf<VoiceChannel>(id = channelId) ?: throw IllegalStateException(
            "Failed to fetch voice channel",
        )
        val members = voiceChannel.voiceStates.toList().map { it.getMember() }

        val usernames = members.joinToString { it.username }
        logger.debug("Members in channel ${channel.data.name.value}, to be split in $groupCount groups: [$usernames]")

        deferredResponse.respond {
            content = "Members: $usernames"
        }
    }

    companion object {
        var logger: Logger = LoggerFactory.getLogger(ShuffleBotService::class.java)
    }
}
