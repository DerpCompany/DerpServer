package com.derpcompany.server.repositories

import com.derpcompany.server.repositories.entities.DiscordChannelEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Repository class for querying within the Mongo DB for Discord Channels
 */
@Repository
interface ChannelRepository : MongoRepository<DiscordChannelEntity, String> {
    /**
     * Query for a channel by ID
     */
    fun findOneByChannelId(channelId: String): DiscordChannelEntity
}
