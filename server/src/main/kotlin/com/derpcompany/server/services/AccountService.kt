package com.derpcompany.server.services

import com.derpcompany.server.controllers.data.AccountResponse
import com.derpcompany.server.controllers.data.Roles
import com.derpcompany.server.controllers.data.toAccountResponse
import com.derpcompany.server.network.models.AccountRequest
import com.derpcompany.server.repositories.AccountRepository
import com.derpcompany.server.repositories.ProfileRepository
import com.derpcompany.server.repositories.entities.Account
import com.derpcompany.server.repositories.entities.Profile
import org.bson.types.ObjectId
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Author: garci
 * Version: 1.0
 * Date: 7/6/2022 11:25
 *
 * Service class for managing authentication for account details
 */

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val profileRepository: ProfileRepository,
) {
    /**
     * Service for querying all accounts
     */
    fun getAllAccounts(): ResponseEntity<List<AccountResponse>> {
        val accounts = accountRepository.findAll().map { it.toAccountResponse() }
        return ResponseEntity.ok(accounts)
    }

    /**
     * Service for querying an account with a validated ID
     */
    fun getOneAccountById(id: String): ResponseEntity<AccountResponse> {
        var account: AccountResponse?

        if (!validIDCheck(id)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        try {
            account = accountRepository.findOneByAccountId(ObjectId(id)).toAccountResponse()
        } catch (e: EmptyResultDataAccessException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        return ResponseEntity.ok(account)
    }

    /**
     * Service for querying an account with a validated username
     */
    fun getOneAccountByUsername(username: String): ResponseEntity<AccountResponse> {
        var account: AccountResponse?

        if (!validUsernameCheck(username)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        try {
            account = accountRepository.findByUsername(username).toAccountResponse()
        } catch (e: EmptyResultDataAccessException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        return ResponseEntity.ok(account)
    }

    fun getAccountsByRole(role: Roles): ResponseEntity<List<AccountResponse>> {
        val accounts = accountRepository.findByRole(role.toString()).map { it.toAccountResponse() }

        return ResponseEntity.ok(accounts)
    }

    fun createAccount(request: AccountRequest): ResponseEntity<AccountResponse> {
        val id = ObjectId()

        val newAccount = Account(
            accountId = id,
            username = request.username,
            email = request.email,
            password = request.password,
            role = Roles.UNAPPROVED,
            createdDate = LocalDateTime.now(),
            modifiedDate = LocalDateTime.now(),
        )

        // create new profile with associated account
        val newProfile = Profile(
            profileId = id,
            username = request.username,
            email = request.email,
            role = Roles.UNAPPROVED,
        )

        accountRepository.save(newAccount)
        profileRepository.save(newProfile)

        return ResponseEntity(newAccount.toAccountResponse(), HttpStatus.CREATED)
    }

    fun updateAccount(request: AccountRequest, id: String): ResponseEntity<AccountResponse> {
        val account = accountRepository.findOneByAccountId(ObjectId(id))
        val profile = profileRepository.findOneByProfileId((ObjectId(id)))

        val updatedAccount = Account(
            accountId = account.accountId,
            username = request.username,
            email = request.email,
            password = account.password,
            role = account.role,
            createdDate = account.createdDate,
            modifiedDate = LocalDateTime.now(),
        )

        // Updated existing profile associated with account
        val updatedProfile = Profile(
            profileId = profile.profileId,
            username = request.username,
            email = request.email,
            role = profile.role,
        )

        accountRepository.save(updatedAccount)
        profileRepository.save(updatedProfile)

        return ResponseEntity.ok(updatedAccount.toAccountResponse())
    }

    fun deleteAccount(id: String): ResponseEntity<AccountResponse> {
        accountRepository.deleteById(id)
        profileRepository.deleteById(id)

        return ResponseEntity.noContent().build()
    }

    /**
     * Validate the ID meets our reqs
     */
    private fun validIDCheck(id: String): Boolean {
        val minLength = 1
        val maxLength = MAX_ID_LENGTH

        // validate id isn't null or empty/blank
        if (id.isBlank()) {
            return false
        }

        id.trim() // trim leading and trailing spaces

        // validate length
        if (id.length < minLength || id.length > maxLength) {
            return false
        }

        // validate there are no spaces in the string
        if (id.contains(" ")) {
            return false
        }

        // TODO: Add special char check

        return true
    }

    /**
     * Validate the Username meets our reqs
     */
    private fun validUsernameCheck(username: String): Boolean {
        val minLength = 2
        val maxLength = MAX_USERNAME_LENGTH

        // validate username isn't null or empty/blank
        if (username.isBlank()) {
            return false
        }

        username.trim() // trim leading and trailing spaces

        // validate username length
        if (username.length < minLength || username.length > maxLength) {
            return false
        }

        // validate there are no spaces in the string
        if (username.contains(" ")) {
            return false
        }

        return true
    }

    companion object {
        private const val MAX_ID_LENGTH = 25
        private const val MAX_USERNAME_LENGTH = 18
    }
}
