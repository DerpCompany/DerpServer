package com.derpcompany.server.service

import com.derpcompany.server.UserRepository
import com.derpcompany.server.entities.User
import com.derpcompany.server.exceptions.NotFoundException
import org.springframework.stereotype.Service

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/17/2022 16:05
 */

@Service
class UserService(private val userRepository: UserRepository) {
    /**
     * Create a new derp user account
     */
    fun createUser(request: User): User {
        return userRepository.save(
            User(
                username = request.username,
                email = request.email,
                role = request.role
            )
        )
    }

    /**
     * Query for all existing derp user accounts
     */
    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    /**
     * Query for a specific user account
     */
    fun findById(id: String): User? {
        return userRepository.findByUserId(id)
    }

    fun updateUser(id: String, request: User): User {
        val userToUpdate = findById(id) ?: throw NotFoundException("User doesn't exist, sorry!")

        val updatedUser = userRepository.save(
            userToUpdate.apply {
                username = request.username
                email = request.email
            }
        )
    }
}