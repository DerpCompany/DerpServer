package com.example.mongodb_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MongodbServerApplication

fun main(args: Array<String>) {
    runApplication<MongodbServerApplication>(*args)
}
