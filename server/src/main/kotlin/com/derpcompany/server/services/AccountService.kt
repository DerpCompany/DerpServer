package com.derpcompany.server.services

import com.derpcompany.server.repositories.AccountRepository
import com.derpcompany.server.repositories.ProfileRepository
import com.derpcompany.server.repositories.entities.AccountEntity
import com.derpcompany.server.repositories.entities.ProfileEntity
import com.derpcompany.server.repositories.entities.Roles
import com.derpcompany.server.services.data.toAccount
import com.derpcompany.server.services.models.Account
import org.bson.types.ObjectId
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Instant
import java.util.regex.Pattern

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
    private val clock: Clock,
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

        private val specialCharPattern: Pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        private val upperCasePattern: Pattern = Pattern.compile("[A-Z ]")
        private val lowerCasePattern: Pattern = Pattern.compile("[a-z ]")
        private val digitCasePattern: Pattern = Pattern.compile("[0-9 ]")
    }

    /**
     * Service for querying all accounts
     */
    fun getAllAccounts(): ResponseEntity<List<Account>> {
        val accounts = accountRepository.findAll().map { it.toAccount() }
        return ResponseEntity.ok(accounts)
    }

    /**
     * Service for querying an account with a validated ID
     */
    fun getOneAccountById(id: String): ResponseEntity<Account> {
        var account: Account?

        if (!validInputCheck(id, MAX_ID_LENGTH, MIN_ID_LENGTH)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        try {
            account = accountRepository.findOneByAccountId(ObjectId(id)).toAccount()
        } catch (e: EmptyResultDataAccessException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        return ResponseEntity.ok(account)
    }

    /**
     * Service for querying an account with a validated username
     */
    fun getOneAccountByUsername(username: String): ResponseEntity<Account> {
        var account: Account?

        if (!validInputCheck(username, MAX_USERNAME_LENGTH, MIN_USERNAME_LENGTH)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        try {
            account = accountRepository.findByUsername(username).toAccount()
        } catch (e: EmptyResultDataAccessException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        return ResponseEntity.ok(account)
    }

    /**
     * Service for querying all accounts with the same role
     */
    fun getAccountsByRole(role: Roles): ResponseEntity<List<Account>> {
        val accounts = accountRepository.findByRole(role.toString()).map { it.toAccount() }
        return ResponseEntity.ok(accounts)
    }

    /**
     * Service for creating a new account, which automatically creates a new profile associated
     * with the new account
     */
    fun createAccount(username: String, email: String, password: String): ResponseEntity<Account> {
        val id = ObjectId()

        // Validate request inputs
        if (!validInputCheck(username, MAX_USERNAME_LENGTH, MIN_USERNAME_LENGTH)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        if (!validEmailCheck(email)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        if (!validPasswordCheck(password)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        // Create account and details
        val newAccount = AccountEntity(
            accountId = id,
            username = username,
            email = email,
            password = password,
            role = Roles.UNAPPROVED,
            createdDate = Instant.now(clock).toEpochMilli(),
            modifiedDate = Instant.now(clock).toEpochMilli(),
        )

        // create new profile with associated account
        val newProfile = ProfileEntity(
            profileId = id,
            username = username,
            email = email,
            role = Roles.UNAPPROVED,
        )

        accountRepository.save(newAccount)
        profileRepository.save(newProfile)

        return ResponseEntity(newAccount.toAccount(), HttpStatus.CREATED)
    }

    /**
     * Service for updating an existing account
     * TODO: Continue working on validating the account exists in the system
     */
    fun updateAccount(username: String, email: String, password: String, id: String): ResponseEntity<Account> {
        // Validate request inputs
        if (validInputCheck(id, MAX_ID_LENGTH, MIN_PASSWORD_LENGTH)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val account = accountRepository.findOneByAccountId(ObjectId(id))
        val profile = profileRepository.findOneByProfileId((ObjectId(id)))

        val updatedAccount = AccountEntity(
            accountId = account.accountId,
            username = username,
            email = email,
            password = account.password,
            role = account.role,
            createdDate = account.createdDate,
            modifiedDate = Instant.now(clock).toEpochMilli(),
        )

        // Updated existing profile associated with account
        val updatedProfile = ProfileEntity(
            profileId = profile.profileId,
            username = username,
            email = email,
            role = profile.role,
        )

        accountRepository.save(updatedAccount)
        profileRepository.save(updatedProfile)

        return ResponseEntity.ok(updatedAccount.toAccount())
    }

    /**
     * Service for deleting an existing account
     * TODO: verify the user owns the account (logged in), we can log to console/file the request, verify action
     * before proceeding
     */
    fun deleteAccount(id: String): ResponseEntity<Account> {
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

        input.trim() // trim leading and trailing spaces

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

        // validate PW contains an approved special char
        if (!specialCharPattern.matcher(password).find()) {
            return false
        }

        // validate PW contains a capital letter
        if (!upperCasePattern.matcher(password).find()) {
            return false
        }
        // validate PW contains a lower case letter
        if (!lowerCasePattern.matcher(password).find()) {
            return false
        }

        // validate PW contains a number
        if (!digitCasePattern.matcher(password).find()) {
            return false
        }

        return true
    }
}
