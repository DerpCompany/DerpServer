package com.derpcompany.server.controllers

import com.derpcompany.server.Qualifiers
import com.derpcompany.server.services.ShuffleBotService
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.kord.rest.builder.interaction.boolean
import dev.kord.rest.builder.interaction.int
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Service

/**
 * Author: cramsan
 * Version: 1.0
 * Date: 12/6/2022 11:25
 *
 * Controller that sets the interface for the ShuffleBot. It takes care of initializing the events and commands to
 * listen to.
 */

@Service
class ShuffleBotController(
    @Qualifier(Qualifiers.SHUFFLE_BOT)
    private val kord: Kord,
    private val scope: CoroutineScope,
    private val shuffleBotService: ShuffleBotService,
) : ApplicationListener<ContextRefreshedEvent> {

    @Suppress("SwallowedException")
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        logger.info("Initializing")
        scope.launch {
            // Register the command to listen to.
            kord.createGlobalChatInputCommand(
                "shuffle",
                "Shuffle the users in this channel into several groups",
            ) {
                int("groups", "How many groups to create") {
                    required = true
                }
                boolean("move", "Create voice channels and move users there") {
                }
            }

            // Configure the callback for any registered commands.
            kord.on<GuildChatInputCommandInteractionCreateEvent> {
                logger.info("GuildChatInputCommandInteractionCreateEvent received")
                logger.debug("Received event data: {}", this.interaction)

                val deferredResponse = interaction.deferPublicResponse()
                val responseBuilder = try {
                    shuffleBotService.handleInteraction(this.interaction)
                } catch (cancellation: CancellationException) {
                    throw cancellation
                } catch (throwable: Throwable) {
                    {
                        content = "There was an exception \uD83D\uDE31"
                    }
                }
                deferredResponse.respond(responseBuilder)
            }

            // Initialize the bot
            kord.login {
                // we need to specify this to receive the content of messages
                @OptIn(PrivilegedIntent::class)
                intents += Intent.MessageContent
            }
            logger.info("Initialization completed")
        }
    }

    companion object {
        var logger: Logger = LoggerFactory.getLogger(ShuffleBotController::class.java)
    }
}
