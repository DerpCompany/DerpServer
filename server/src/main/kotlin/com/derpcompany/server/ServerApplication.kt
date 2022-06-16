package com.derpcompany.server

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(LoginProperties::class)
class ServerApplication

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args) {
		setBannerMode(Banner.Mode.OFF)
	}
}
