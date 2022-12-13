package com.derpcompany.server.services

import dev.kord.common.DiscordBitSet
import dev.kord.common.Locale
import dev.kord.common.entity.*
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.OptionalBoolean
import dev.kord.common.entity.optional.OptionalInt
import dev.kord.common.entity.optional.OptionalSnowflake
import dev.kord.core.Kord
import dev.kord.core.cache.data.*
import dev.kord.core.entity.Member
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.supplier.EntitySupplier
import kotlinx.datetime.Instant

object ShuffleBotData {
    const val defaultUsername = "cramsan"

    val defaultGuildId = Snowflake(827619747428040704)

    val defaultChannelId = Snowflake(827619747867656194)

    val defaultMemberId = Snowflake(218224841063530498)

    val defaultApplicationId = Snowflake(1052119649334075393)

    val memberId2 = Snowflake(234984237234982349)

    val memberId3 = Snowflake(215859594303040549)

    val defaultInteractionData = InteractionData(
        id= Snowflake(1052138292876939275),
        applicationId= defaultApplicationId,
        type= InteractionType.ApplicationCommand,
        data= ApplicationInteractionData(
            id= OptionalSnowflake.Value(Snowflake(1052121125980745728)),
            type= Optional.Value(ApplicationCommandType.ChatInput),
            name= Optional.Value("shuffle"),
            options= Optional.Value(
                listOf(
                    OptionData(
                        name="groups",
                        value= Optional.Value(
                            CommandArgument.IntegerArgument(
                                name="groups",
                                value=10,
                                focused= OptionalBoolean.Missing
                            )),
                    )
                )
            ),
        ),
        guildId= OptionalSnowflake.Value(defaultGuildId),
        channelId= defaultChannelId,
        member= Optional.Value(
            MemberData(
                userId= defaultMemberId,
                guildId= defaultGuildId,
                roles= listOf(),
                joinedAt= Instant.parse("2021-04-02T19:05:24.270Z"),
            )
        ),
        user= Optional.Value(
            UserData(
                id= defaultMemberId,
                username=defaultUsername,
                discriminator="5835",
                avatar="6e6f0061cb943b4148a09c496cf3f483",
            )
        ),
        token="aW50Z.....0RuSg",
        permissions= Optional.Value(
            Permissions(
                DiscordBitSet("111111111111111111111111111111111111111111")
            )
        ),
        version=1,
        appPermissions= Optional.Value(
            Permissions(
                DiscordBitSet("111111111111111111111111111111111111111111")
            )
        ),
        locale= Optional.Value(
            Locale(language="en", country="US")
        ),
        guildLocale= Optional.Value(Locale(language="en", country="US")),
    )

    fun createTestVoiceStateData(
        userId: Snowflake = defaultMemberId,
        guildId: Snowflake = defaultGuildId,
        channelId: Snowflake = defaultChannelId,
    ) = VoiceStateData(
        guildId=guildId,
        channelId=channelId,
        userId=userId,
        sessionId="35e5daac99b945098067d6851cd6d83d",
        deaf=false,
        mute=false,
        selfDeaf=false,
        selfMute=false,
        suppress=false,
        selfVideo=false,
        selfStream=OptionalBoolean.Missing,
        requestToSpeakTimestamp=null
    )

    fun createTestChannel(
        guildId: Snowflake = defaultGuildId,
        channelId: Snowflake = defaultChannelId,
        kord: Kord,
        supplier: EntitySupplier,
    ) = VoiceChannel(
        data=ChannelData(
            id=channelId,
            type=ChannelType.GuildVoice,
            guildId=OptionalSnowflake.Value(guildId),
            position= OptionalInt.Value(8),
            name=Optional.Value("Lounge"),
            lastMessageId=OptionalSnowflake.Value(Snowflake(1052153716909817907)),
            bitrate= OptionalInt.Value(value=64000),
            parentId=OptionalSnowflake.Value(Snowflake(827619747867656193)),
        ),
        kord=kord,
        supplier = supplier,
    )

    fun createTestMember(
        userId: Snowflake,
        username: String,
        guildId: Snowflake = defaultGuildId,
        kord: Kord,
        supplier: EntitySupplier,
    ) = Member(
        memberData=MemberData(
            userId=userId,
            guildId=guildId,
            roles= listOf(),
            joinedAt=Instant.parse("2021-04-02T19:05:24.270Z"),
        ),
        userData=UserData(
            id=userId,
            username=username,
            discriminator="5835",
            avatar="6e6f0061cb943b4148a09c496cf3f483",
        ),
        kord,
        supplier,
    )


}