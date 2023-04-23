package com.derpcompany.server.services

import com.derpcompany.server.Qualifiers
import com.derpcompany.server.repositories.ChannelRepository
import com.derpcompany.server.services.data.toDiscordChannel
import com.derpcompany.server.services.data.toEntity
import dev.kord.common.entity.DiscordChannel
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.value
import dev.kord.core.Kord
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.rest.service.createVoiceChannel
import io.ktor.util.logging.error
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import kotlin.random.Random

/**
 * Author: cramsan
 * Version: 1.0
 * Date: 12/6/2022 11:25
 *
 * Service that controls the creation and management of channels.
 */

@Service
class DiscordChannelService(
    @Qualifier(Qualifiers.SHUFFLE_BOT)
    private val kord: Kord,
    private val random: Random,
    private val channelRepository: ChannelRepository,
    private val scope: CoroutineScope,
) {

    @Scheduled(cron = "0 */2 * ? * *")
    fun cleanUp() {
        scope.launch {
            val channels = channelRepository.findAll()
            logger.info("Inspecting ${channels.size} channels")

            channels.forEach {
                try {
                    val savedChannel = it.toDiscordChannel()
                    val channelId = savedChannel.id

                    val channel = kord.getChannel(id = channelId) ?: throw IllegalStateException(
                        "Failed to fetch channel",
                    )
                    val channelName = channel.data.name.value
                    if (channelName?.startsWith(CHANNEL_PREFIX) != true) {
                        throw IllegalStateException("Channel does not match expected name format")
                    }
                    // Fetch the channel, for real this time.
                    val voiceChannel = kord.getChannelOf<VoiceChannel>(id = channelId) ?: throw IllegalStateException(
                        "Failed to fetch voice channel",
                    )
                    // Get the list of members in this channel
                    val memberCount = voiceChannel.voiceStates.toList().size
                    if (memberCount > 0) {
                        return@forEach
                    }

                    logger.info("Channel $channelName is empty. Deleting.")
                    kord.rest.channel.deleteChannel(
                        channelId = savedChannel.id,
                    )
                } catch (t: Throwable) {
                    logger.error("Error while cleaning up channel: ${it.name}(${it.channelId})")
                    logger.error(t)
                } catch (c: CancellationException) {
                    throw c
                }
            }
        }
    }
    suspend fun createTempVoiceChannel(guildId: Snowflake, parentId: Snowflake?): DiscordChannel {
        val name = "$CHANNEL_PREFIX${adjectives.random(random)} ${animals.random(random)}"

        logger.info("Creating temp voice channel $name")
        val voiceChannel = kord.rest.guild.createVoiceChannel(
            guildId,
            name,
        ) {
            this.parentId = parentId
        }

        val channelEntity = voiceChannel.toEntity()
        channelRepository.insert(channelEntity)
        logger.debug("Saved temp voice channel {} to repository", name)

        return voiceChannel
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DiscordChannelService::class.java)

        private const val CHANNEL_PREFIX = "*"

        private val adjectives = listOf(
            "Adorable",
            "Adventurous",
            "Aggressive",
            "Agreeable",
            "Alert",
            "Alive",
            "Amused",
            "Angry",
            "Annoyed",
            "Annoying",
            "Anxious",
            "Arrogant",
            "Ashamed",
            "Attractive",
            "Average",
            "Awful",
            "Bad",
            "Beautiful",
            "Better",
            "Bewildered",
            "Bloody",
            "Blue",
            "Blue-eyed",
            "Blushing",
            "Bored",
            "Brainy",
            "Brave",
            "Breakable",
            "Bright",
            "Busy",
            "Calm",
            "Careful",
            "Cautious",
            "Charming",
            "Cheerful",
            "Clean",
            "Clear",
            "Clever",
            "Cloudy",
            "Clumsy",
            "Colorful",
            "Combative",
            "Comfortable",
            "Concerned",
            "Condemned",
            "Confused",
            "Cooperative",
            "Courageous",
            "Crazy",
            "Creepy",
            "Crowded",
            "Cruel",
            "Curious",
            "Cute",
            "dangerous",
            "Dark",
            "Dead",
            "Defeated",
            "Defiant",
            "Delightful",
            "Depressed",
            "Determined",
            "Different",
            "Difficult",
            "Distinct",
            "Disturbed",
            "Dizzy",
            "Doubtful",
            "Drab",
            "Dull",
            "Eager",
            "Easy",
            "Elated",
            "Elegant",
            "Embarrassed",
            "Enchanting",
            "Encouraging",
            "Energetic",
            "Enthusiastic",
            "Envious",
            "Evil",
            "Excited",
            "Expensive",
            "Exuberant",
            "Fair",
            "Faithful",
            "Famous",
            "Fancy",
            "Fantastic",
            "Fierce",
            "Filthy",
            "Fine",
            "Foolish",
            "Fragile",
            "Frail",
            "Frantic",
            "Friendly",
            "Frightened",
            "Funny",
            "Gentle",
            "Gifted",
            "Glamorous",
            "Gleaming",
            "Glorious",
            "Good",
            "Gorgeous",
            "Graceful",
            "Grieving",
            "Grotesque",
            "Grumpy",
            "Handsome",
            "Happy",
            "Healthy",
            "Helpful",
            "Helpless",
            "Hilarious",
            "Homeless",
            "Homely",
            "Horrible",
            "Hungry",
            "Hurt",
            "Ill",
            "Important",
            "Impossible",
            "Inexpensive",
            "Innocent",
            "Inquisitive",
            "Itchy",
            "Jealous",
            "Jittery",
            "Jolly",
            "Joyous",
            "Kind",
            "Lazy",
            "Light",
            "Lively",
            "Lonely",
            "Long",
            "Lovely",
            "Lucky",
            "Magnificent",
            "Misty",
            "Modern",
            "Motionless",
            "Muddy",
            "Mushy",
            "Mysterious",
            "Nasty",
            "Naughty",
            "Nervous",
            "Nice",
            "Nutty",
            "Obedient",
            "Obnoxious",
            "Odd",
            "Old-fashioned",
            "Open",
            "Outrageous",
            "Outstanding",
            "Panicky",
            "Perfect",
            "Plain",
            "Pleasant",
            "Poised",
            "Poor",
            "Powerful",
            "Precious",
            "Prickly",
            "Proud",
            "Putrid",
            "Puzzled",
            "Quaint",
            "Real",
            "Relieved",
            "Repulsive",
            "Rich",
            "Scary",
            "Selfish",
            "Shiny",
            "Shy",
            "Silly",
            "Sleepy",
            "Smiling",
            "Smoggy",
            "Sore",
            "Sparkling",
            "Splendid",
            "Spotless",
            "Stormy",
            "Strange",
            "Stupid",
            "Successful",
            "Super",
            "Talented",
            "Tame",
            "Tasty",
            "Tender",
            "Tense",
            "Terrible",
            "Thankful",
            "Thoughtful",
            "Thoughtless",
            "Tired",
            "Tough",
            "Troubled",
            "Ugliest",
            "Ugly",
            "Uninterested",
            "Unsightly",
            "Unusual",
            "Upset",
            "Uptight",
            "Vast",
            "Victorious",
            "Vivacious",
            "Wandering",
            "Weary",
            "Wicked",
            "Wide-eyed",
            "Wild",
            "Witty",
            "Worried",
            "Worrisome",
            "Wrong",
            "Zany",
            "Zealous",
        )

        private val animals = listOf(
            "Frog",
            "Crocodile",
            "Alligator",
            "Monitor Lizard",
            "Salamander",
            "Toad",
            "Newt",
            "Iguana",
            "Snake",
            "Snake",
            "Lion",
            "Tiger",
            "Goat",
            "Horse",
            "Donkey",
            "Dog",
            "Cat",
            "Pig",
            "Panther",
            "Leopard",
            "Cheetah",
            "Cow",
            "Walrus",
            "Otter",
            "Giraffe",
            "Sheep",
            "Rabbit",
            "Monkey",
            "Snake",
            "Crocodile",
            "Alligator",
            "Tortoise",
            "Turtle",
            "Lizard",
            "Chameleon",
            "Basilisk",
            "Moccasin",
            "Gecko",
            "Herring",
            "Crab",
            "Brill",
            "Haddock",
            "Eel",
            "Whale",
            "Blue Whale",
            "Salmon",
            "Sardines",
            "Pike",
            "Carp",
            "Shark",
            "Tuna",
            "Pufferfish",
            "Blue Tang",
            "Flamingo",
            "Crow",
            "Hen",
            "Vulture",
            "Eagle",
            "Peacock",
            "Pigeon",
            "Emu",
            "Ostrich",
            "Dove",
            "Stork",
        )
    }
}
