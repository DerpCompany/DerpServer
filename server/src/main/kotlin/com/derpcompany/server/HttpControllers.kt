package com.derpcompany.server

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/15/2022 16:19
 */

/**
 * HTTP API for articles
 */
@RestController
@RequestMapping("/api/article")
class AricleController(private val repository: ArticleRepository) {

    @GetMapping("/")
    fun findAll(): Iterable<Article> {
        return repository.findAllByOrderByAddedAtDesc()
    }

    @GetMapping("/{slug}")
    fun findOne(@PathVariable slug: String): Article {
        return repository.findBySlug(slug) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "This article doesn't exist"
        )
    }
}

/**
 * HTTP API for users
 */
@RestController
@RequestMapping("/api/user")
class UserController(private val repository: UserRepository) {
    @GetMapping("/")
    fun findAll(): Iterable<User> {
        return repository.findAll()
    }

    @GetMapping("/{username}")
    fun findOne(@PathVariable username: String): User {
        return repository.findByUsername(username) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "This user doesn't exist"
        )
    }
}