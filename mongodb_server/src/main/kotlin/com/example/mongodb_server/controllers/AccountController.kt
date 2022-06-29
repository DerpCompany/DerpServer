package com.example.mongodb_server.controllers

import com.example.mongodb_server.controllers.data.*
import com.example.mongodb_server.repositories.entities.Account
import com.example.mongodb_server.repositories.AccountRepository
import com.example.mongodb_server.repositories.ProfileRepository
import com.example.mongodb_server.repositories.entities.Profile
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping ("/api")
class AccountController(private val accountRepository: AccountRepository, private val profileRepository: ProfileRepository) {
    /**
     * Create new  account
     */
    @PostMapping("/account")
    fun createAccount(@RequestBody request: AccountRequest): ResponseEntity<AccountResponse> {
        val id = ObjectId()

        val newUser = Account(
            accountId = id,
            username = request.username,
            email = request.email,
            password = request.password,
            role = "unapproved",
            createdDate = LocalDateTime.now(),
            modifiedDate = LocalDateTime.now(),
        )

        val newProfile = Profile(
            profileId = id,
            username = request.username,
            email = request.email,
            role = "unapproved",
        )

        accountRepository.save(newUser)
        profileRepository.save(newProfile)

        return ResponseEntity(newUser.toAccountResponse(), HttpStatus.CREATED)
    }

    /**
     * Update an existing account
     */
    @PutMapping("/account/{id}")
    fun updateAccount(@RequestBody request: AccountRequest, @PathVariable("id") id: String): ResponseEntity<AccountResponse> {
        val account = accountRepository.findOneByAccountId(ObjectId(id))
        val profile = profileRepository.findOneByProfileId((ObjectId(id)))

        val updatedUser = Account(
            accountId = account.accountId,
            username = request.username,
            email = request.email,
            password = account.password,
            role = account.role,
            createdDate = account.createdDate,
            modifiedDate = LocalDateTime.now(),
        )

        val updatedProfile = Profile(
            profileId = profile.profileId,
            username = request.username,
            email = request.email,
            role = profile.role,
        )

        accountRepository.save(updatedUser)
        profileRepository.save(updatedProfile)

        return ResponseEntity.ok(updatedUser.toAccountResponse())
    }

    /**
     * Delete an existing account
     * TODO: Should have have a verification with a body request (AccountRequest)?
     */
    @DeleteMapping("/account/{id}")
    fun deleteAccount(@PathVariable("id") id: String): ResponseEntity<AccountResponse> {
        accountRepository.deleteById(id)
        profileRepository.deleteById(id)

        return ResponseEntity.noContent().build()
    }
}