package com.derpcompany.server

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
    fun databaseInitializer(userRepository: UserRepository, articleRepository: ArticleRepository) = ApplicationRunner {
        val cramsan = userRepository.save(User("cramsan", "cramsan@gmail.com", "admin", "Discord"))
        articleRepository.save(
            Article(
                title = "Reactor Bismuth is out",
                headline = "Lorem ipsum",
                content = "dolor sit amet",
                author = cramsan
            )
        )

        articleRepository.save(
            Article(
                title = "Reactor Aluminium has landed",
                headline = "Lorem ipsum",
                content = "dolor sit amet",
                author = cramsan
            )
        )
    }
}