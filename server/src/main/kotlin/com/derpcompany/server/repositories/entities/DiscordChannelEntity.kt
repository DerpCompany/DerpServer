package com.derpcompany.server.repositories.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Author: cramsan
 * Version: 1.0
 * Date: 4/22/2023 14:27
 *
 * Representation of a channel in the repo layer.
 */
@Document("discordChannel")
data class DiscordChannelEntity(
    @Id
    val id: ObjectId,
    val channelId: String,
    val guildId: String,
    val type: Int,
    val name: String,
    val parentId: String,
)
