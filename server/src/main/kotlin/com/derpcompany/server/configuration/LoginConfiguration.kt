package com.derpcompany.server.configuration

import UserRepository
import com.derpcompany.server.entities.User
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/15/2022 16:04
 */

@Configuration
class LoginConfiguration {

    @Bean
    fun databaseInitializer(userRepository: UserRepository) = ApplicationRunner {
        val cramsan = userRepository.save(User("cramsan", "cramsan@gmail.com", "admin"))
    }
}