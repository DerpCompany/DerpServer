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
@RequestMapping ("/api")
class AccountController(private val accountRepository: AccountRepository) {

    /**
     * Query all profiles. Excludes passwords
     */
    @GetMapping("/profile")
    fun getAllProfiles(): ResponseEntity<List<ProfileResponse>> {
        val users = accountRepository.findAll().map { it.toProfileResponse() }
        return ResponseEntity.ok(users)
    }

    /**
     * Query profile by ID. Excludes passwords
     */
    @GetMapping("/profile/{id}")
    fun getOneProfileById(@PathVariable("id") id: String): ResponseEntity<ProfileResponse> {
        val user = accountRepository.findOneByUserId(ObjectId(id)).toProfileResponse()
        return ResponseEntity.ok(user)
    }

    /**
     * Query user by username. Excludes passwords
     */
    @GetMapping("/profile/username/{username}")
    fun getOneProfileByUsername(@PathVariable("username") username: String): ResponseEntity<ProfileResponse> {
        val user = accountRepository.findByUsername(username).toProfileResponse()
        return ResponseEntity.ok(user)
    }

    /**
     * Query all profiles with specific role. Excludes passwords
     */
    @GetMapping("/profile/role/{role}")
    fun getProfilesByRole(@PathVariable("role") role: String): ResponseEntity<List<ProfileResponse>> {
        val users = accountRepository.findByRole(role).map { it.toProfileResponse() }
        return ResponseEntity.ok(users)
    }

    /**
     * Create new  account
     */
    @PostMapping("/account")
    fun createAccount(@RequestBody request: AccountRequest): ResponseEntity<ProfileResponse> {
        val newUser = (Account(
            accountId = ObjectId(),
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
    @PutMapping("/account/{id}")
    fun updateAccount(@RequestBody request: AccountRequest, @PathVariable("id") id: String): ResponseEntity<ProfileResponse> {
        val user = accountRepository.findOneByUserId(ObjectId(id))

        val updatedUser = (Account(
            accountId = user.accountId,
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
    @DeleteMapping("/account/{id}")
    fun deleteAccount(@PathVariable("id") id: String): ResponseEntity<ProfileResponse> {
        accountRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}