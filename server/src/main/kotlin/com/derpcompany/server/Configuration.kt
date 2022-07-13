package com.derpcompany.server

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
}
