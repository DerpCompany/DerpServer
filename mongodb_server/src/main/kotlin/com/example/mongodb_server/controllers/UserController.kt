package com.example.mongodb_server.controllers

import com.example.mongodb_server.controllers.data.*
import com.example.mongodb_server.repositories.entities.Account
import com.example.mongodb_server.repositories.AccountRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping ("/api/user")
class UserController(private val accountRepository: AccountRepository) {

    /**
     * Query all site users
     */
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<ProfileResponse>> {
        val users = accountRepository.findAll().map { it.toProfileResponse() }
        return ResponseEntity.ok(users)
    }

    /**
     * Query user by ID
     */
    @GetMapping("/id/{id}")
    fun getOneUserByUserId(@PathVariable("id") id: String): ResponseEntity<ProfileResponse> {
        val user = accountRepository.findOneByUserId(ObjectId(id)).toProfileResponse()
        return ResponseEntity.ok(user)
    }

    /**
     * Query user by username
     */
    @GetMapping("/username/{username}")
    fun getOneUserByUsername(@PathVariable("username") username: String): ResponseEntity<ProfileResponse> {
        val user = accountRepository.findByUsername(username).toProfileResponse()
        return ResponseEntity.ok(user)
    }

    /**
     * Query all users with specific role
     */
    @GetMapping("/roles/{role}")
    fun getUsersByRoles(@PathVariable("role") role: String): ResponseEntity<List<ProfileResponse>> {
        val users = accountRepository.findByRole(role).map { it.toProfileResponse() }
        return ResponseEntity.ok(users)
    }

    /**
     * Create new user account
     */
    @PostMapping
    fun createAccount(@RequestBody request: NewAccount): ResponseEntity<ProfileResponse> {
        val newUser = (Account(
            userId = ObjectId(),
            username = request.username,
            email = request.email,
            password = request.password,
            role = "unapproved",
            createdDate = LocalDateTime.now(),
            modifiedDate = LocalDateTime.now(),
        ))

        accountRepository.save(newUser)
        return ResponseEntity(newUser.toProfileResponse(), HttpStatus.CREATED)
    }

    /**
     * Update an existing account
     */
    @PutMapping("/{id}")
    fun updateAccount(@RequestBody request: NewAccount, @PathVariable("id") id: String): ResponseEntity<ProfileResponse> {
        val user = accountRepository.findOneByUserId(ObjectId(id))

        val updatedUser = (Account(
            userId = user.userId,
            username = request.username,
            email = request.email,
            password = request.password,
            role = user.role,
            createdDate = user.createdDate,
            modifiedDate = LocalDateTime.now(),
        ))

        accountRepository.save(updatedUser)
        return ResponseEntity.ok(updatedUser.toProfileResponse())
    }

    /**
     * Delete an existing account
     */
    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable("id") id: String): ResponseEntity<ProfileResponse> {
        accountRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}