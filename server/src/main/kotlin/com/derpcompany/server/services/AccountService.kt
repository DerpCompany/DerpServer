package com.derpcompany.server.services

import com.derpcompany.server.ConstantStrings
import com.derpcompany.server.controllers.data.toAccountResponse
import com.derpcompany.server.network.models.AccountRequest
import com.derpcompany.server.network.models.AccountResponse
import com.derpcompany.server.network.models.Roles
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
     * Constant values for validation checks
     */
    companion object {
        private const val MAX_ID_LENGTH = 25
        private const val MIN_ID_LENGTH = 1

        private const val MAX_USERNAME_LENGTH = 18
        private const val MIN_USERNAME_LENGTH = 2

        private const val MAX_PASSWORD_LENGTH = 20
        private const val MIN_PASSWORD_LENGTH = 8

        private val emailPattern =
            ("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
                .toRegex()

        private val passwordAcceptedSpecialChars = ("[!@#$^&*-?_`']").toRegex()
    }

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

        if (!validInputCheck(id, MAX_ID_LENGTH, MIN_ID_LENGTH)) {
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

        if (!validInputCheck(username, MAX_USERNAME_LENGTH, MIN_USERNAME_LENGTH)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        try {
            account = accountRepository.findByUsername(username).toAccountResponse()
        } catch (e: EmptyResultDataAccessException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        return ResponseEntity.ok(account)
    }

    /**
     * Service for querying all accounts with the same role
     */
    fun getAccountsByRole(role: Roles): ResponseEntity<List<AccountResponse>> {
        val accounts = accountRepository.findByRole(role.toString()).map { it.toAccountResponse() }
        return ResponseEntity.ok(accounts)
    }

    /**
     * Service for creating a new account, which automatically creates a new profile associated
     * with the new account
     */
    fun createAccount(request: AccountRequest): ResponseEntity<AccountResponse> {
        val id = ObjectId()

        // Validate request inputs
        if (!validInputCheck(request.username, MAX_USERNAME_LENGTH, MIN_USERNAME_LENGTH)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        if (!validEmailCheck(request.email)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        if (!validPasswordCheck(request.password)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        // Create account and details
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

    /**
     * Service for updating an existing account
     * TODO: Continue working on validating the account exists in the system
     */
    fun updateAccount(request: AccountRequest, id: String): ResponseEntity<AccountResponse> {
        // Validate request inputs
        if (validInputCheck(id, MAX_ID_LENGTH, MIN_PASSWORD_LENGTH)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

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

    /**
     * Service for deleting an existing account
     * TODO: verify the user owns the account (logged in), we can log to console/file the request, verify action
     * before proceeding
     */
    fun deleteAccount(request: AccountRequest, id: String): ResponseEntity<AccountResponse> {
        accountRepository.deleteById(id)
        profileRepository.deleteById(id)

        return ResponseEntity.noContent().build()
    }

    /**
     * Validate the input meets our business logic of not being null,
     * expected lengths, and doesn't contain spaces
     */
    private fun validInputCheck(input: String, maxLength: Int, minLength: Int): Boolean {

        // validate id isn't null or empty/blank
        if (input.isBlank()) {
            return false
        }

        input.trim() //trim leading and trailing spaces

        // validate length
        if (input.length < minLength || input.length > maxLength) {
            return false
        }

        // validate there are no spaces in the string
        if (input.contains(" ")) {
            return false
        }

        return true
    }

    /**
     * Validate a valid email has been entered
     */
    private fun validEmailCheck(email: String): Boolean {
        // validate email isn't blank/empty
        if (email.isBlank()) {
            return false
        }

        email.trim()

        // return true/false if the email matches regex Patter for email
        return emailPattern.matches(email)
    }

    /**
     * Validate email is secure and meets min requirements
     */
    private fun validPasswordCheck(password: String): Boolean {
        // validate password isn't blank/empty
        if (password.isBlank()) {
            return false
        }

        // validate password is within range
        if (password.length < MIN_PASSWORD_LENGTH || password.length > MAX_PASSWORD_LENGTH) {
            return false
        }

        // validate PW contains a lower case letter
        if (!password.matches("[a-z]".toRegex())) {
            return false
        }

        // validate PW contains a capital letter
        if (!password.matches("[A-Z]".toRegex())) {
            return false
        }

        // validate PW contains a number
        if (!password.matches("\\d".toRegex())) {
            return false
        }

        // validate PW contains an approved special char
        if (!password.matches((passwordAcceptedSpecialChars))) {
            return false
        }

        return true
    }

    companion object {
        private const val MAX_ID_LENGTH = 25
        private const val MAX_USERNAME_LENGTH = 18
    }
}
