package com.derpcompany.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}
