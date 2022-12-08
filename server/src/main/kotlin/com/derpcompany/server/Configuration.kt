package com.derpcompany.server

import dev.kord.core.Kord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

/**
 * Author: cramsan
 *
 * Use this class to configure beans and to configure the DI.
 */

@Configuration
class Configuration {

    /**
     * Provide a [Clock] instance. This is useful to ensure we can inject a clock for testing purposes. In production
     * this [Clock] is configured to follow the system's time.
     */
    @Bean
    fun clock(): Clock {
        return Clock.systemUTC()
    }

    @Value("\${shufflebot.token}")
    lateinit var shuffleBotToken: String

    /**
     * Provide a Kord instance for the ShuffleBot.
     */
    @Qualifier(Qualifiers.SHUFFLE_BOT)
    @Bean
    fun kord(): Kord = runBlocking { Kord(shuffleBotToken) }

    /**
     * Coroutine scope to be used globally.
     */
    @Bean
    fun scope(): CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
}
