package com.derpcompany.server.controllers

import com.derpcompany.server.network.models.AccountResponse
import com.derpcompany.server.network.models.Roles
import com.derpcompany.server.network.models.AccountRequest
import com.derpcompany.server.services.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AccountController(
    private val accountService: AccountService,
) {
    /**
     * Query all accounts.
     */
    @GetMapping("/account")
    fun getAllAccounts(): ResponseEntity<List<AccountResponse>> {
        return accountService.getAllAccounts()
    }

    /**
     * Query account by ID.
     */
    @GetMapping("/account/{id}")
    fun getOneAccountById(@PathVariable("id") id: String): ResponseEntity<AccountResponse> {
        return accountService.getOneAccountById(id)
    }

    /**
     * Query account by username.
     */
    @GetMapping("/account/username/{username}")
    fun getOneAccountByUsername(@PathVariable("username") username: String): ResponseEntity<AccountResponse> {
        return accountService.getOneAccountByUsername(username)
    }

    /**
     * Query all accounts with specific role
     */
    @GetMapping("/account/role/{role}")
    fun getAccountsByRole(@PathVariable("role") role: Roles): ResponseEntity<List<AccountResponse>> {
        return accountService.getAccountsByRole(role)
    }

    /**
     * Create new account
     */
    @PostMapping("/account")
    fun createAccount(@RequestBody request: AccountRequest): ResponseEntity<AccountResponse> {
        return accountService.createAccount(request)
    }

    /**
     * Update an existing account
     */
    @PutMapping("/account/{id}")
    fun updateAccount(
        @RequestBody request: AccountRequest,
        @PathVariable("id") id: String,
    ): ResponseEntity<AccountResponse> {
        return accountService.updateAccount(request, id)
    }

    /**
     * Delete an existing account
     * TODO: Should have have a verification with a body request (AccountRequest)?
     */
    @DeleteMapping("/account/{id}")
    fun deleteAccount(@RequestBody request: AccountRequest,
                      @PathVariable("id") id: String): ResponseEntity<AccountResponse> {
        return accountService.deleteAccount(request, id)
    }
}
