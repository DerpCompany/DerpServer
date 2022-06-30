package com.derpcompany.server.integrationtests

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.derpcompany.server")
class IntegrationServerApplication

fun main(args: Array<String>) {
    runApplication<IntegrationServerApplication>(*args)
}
