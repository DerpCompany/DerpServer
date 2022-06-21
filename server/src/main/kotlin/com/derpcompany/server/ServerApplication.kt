package com.derpcompany.server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableConfigurationProperties(LoginProperties::class)
@EnableMongoRepositories
class ServerApplication

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args) {
		setBannerMode(Banner.Mode.OFF)
	}
}
