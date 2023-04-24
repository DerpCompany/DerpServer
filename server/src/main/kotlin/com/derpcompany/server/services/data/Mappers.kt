package com.derpcompany.server.services.data

import com.derpcompany.server.controllers.data.toRolesWireType
import com.derpcompany.server.repositories.entities.AccountEntity
import com.derpcompany.server.repositories.entities.DiscordChannelEntity
import com.derpcompany.server.repositories.entities.ProfileEntity
import com.derpcompany.server.services.models.Account
import com.derpcompany.server.services.models.Profile
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.DiscordChannel
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.OptionalSnowflake
import dev.kord.common.entity.optional.value
import org.bson.types.ObjectId
import java.time.Instant

/**
 * Shows the account details excluding sensitive data like password
 * TODO: Verify is we want to exclude email
 */
fun AccountEntity.toAccount(): Account {
    return Account(
        accountId = accountId.toHexString(),
        username,
        email,
        role.toRolesWireType(),
        password,
        createdDate = Instant.ofEpochMilli(createdDate),
        modifiedDate = Instant.ofEpochMilli(modifiedDate),
    )
}

/**
 * Shows the profile details
 * TODO: Verify is we want to exclude email
 */
fun ProfileEntity.toProfile(): Profile {
    return Profile(
        profileId = profileId.toHexString(),
        username,
        email,
        role.toRolesWireType(),
    )
}

/**
 */
fun DiscordChannel.toEntity(): DiscordChannelEntity {
    return DiscordChannelEntity(
        id = ObjectId(),
        channelId = id.value.toString(),
        guildId = (guildId.value?.value ?: Snowflake.min.value).toString(),
        type = type.value,
        name = name.value ?: "*",
        parentId = (parentId.value?.value ?: Snowflake.min.value).toString(),
    )
}

fun DiscordChannelEntity.toDiscordChannel(): DiscordChannel {
    return DiscordChannel(
        id = Snowflake(channelId),
        guildId = OptionalSnowflake.Value(guildId.toULong()),
        type = mapIntTOChannelType(type),
        name = Optional(name),
        parentId = OptionalSnowflake.Value(parentId.toULong()),
    )
}

@Suppress("MagicNumber")
private fun mapIntTOChannelType(value: Int) = when (value) {
    0 -> ChannelType.GuildText
    1 -> ChannelType.DM
    2 -> ChannelType.GuildVoice
    3 -> ChannelType.GroupDM
    4 -> ChannelType.GuildCategory
    5 -> ChannelType.GuildNews
    6 -> @Suppress("DEPRECATION_ERROR") (ChannelType.GuildStore)
    10 -> ChannelType.PublicNewsThread
    11 -> ChannelType.PublicGuildThread
    12 -> ChannelType.PrivateThread
    13 -> ChannelType.GuildStageVoice
    14 -> ChannelType.GuildDirectory
    else -> ChannelType.Unknown(value)
}
