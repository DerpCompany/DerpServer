package com.derpcompany.server.html

import com.derpcompany.server.UserRepository
import com.derpcompany.server.entities.User
import com.derpcompany.server.exceptions.NotFoundException
import com.derpcompany.server.service.UserService
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
 * HTTP API for users
 * TODO: Update based on mongodb
 */
@RestController
@RequestMapping("/api/user")
class UserApi(private val userService: UserService) {
    @GetMapping("/")
    fun findAll(): Iterable<User> {
        return userService.findAll()
    }

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: String): User {
        return userService.findById(id) ?: throw NotFoundException(
            "This user doesn't exist"
        )
    }
}