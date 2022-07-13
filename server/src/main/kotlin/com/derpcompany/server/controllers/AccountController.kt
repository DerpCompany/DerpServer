package com.derpcompany.server.controllers

import com.derpcompany.server.controllers.data.convertAccountListToResponse
import com.derpcompany.server.controllers.data.mapBody
import com.derpcompany.server.controllers.data.toAccountResponse
import com.derpcompany.server.controllers.data.toRoles
import com.derpcompany.server.network.wiretypes.AccountListResponse
import com.derpcompany.server.network.wiretypes.AccountRequest
import com.derpcompany.server.network.wiretypes.AccountResponse
import com.derpcompany.server.network.wiretypes.RolesWireType
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
    fun getAllAccounts(): ResponseEntity<AccountListResponse?> {
        return accountService.getAllAccounts().mapBody {
            convertAccountListToResponse(it)
        }
    }

    /**
     * Query account by ID.
     */
    @GetMapping("/account/{id}")
    fun getOneAccountById(@PathVariable("id") id: String): ResponseEntity<AccountResponse?> {
        return accountService.getOneAccountById(id).mapBody {
            it?.toAccountResponse()
        }
    }

    /**
     * Query account by username.
     */
    @GetMapping("/account/username/{username}")
    fun getOneAccountByUsername(@PathVariable("username") username: String): ResponseEntity<AccountResponse?> {
        return accountService.getOneAccountByUsername(username).mapBody {
            it?.toAccountResponse()
        }
    }

    /**
     * Query all accounts with specific role
     */
    @GetMapping("/account/role/{role}")
    fun getAccountsByRole(@PathVariable("role") role: RolesWireType): ResponseEntity<AccountListResponse?> {
        return accountService.getAccountsByRole(role.toRoles()).mapBody {
            convertAccountListToResponse(it)
        }
    }

    /**
     * Create new account
     */
    @PostMapping("/account")
    fun createAccount(@RequestBody request: AccountRequest): ResponseEntity<AccountResponse?> {
        return accountService.createAccount(
            username = request.username,
            email = request.email,
            password = request.password,
        ).mapBody {
            it?.toAccountResponse()
        }
    }

    /**
     * Update an existing account
     */
    @PutMapping("/account/{id}")
    fun updateAccount(
        @RequestBody request: AccountRequest,
        @PathVariable("id") id: String,
    ): ResponseEntity<AccountResponse?> {
        return accountService.updateAccount(
            username = request.username,
            email = request.email,
            password = request.password,
            id = id,
        ).mapBody {
            it?.toAccountResponse()
        }
    }

    /**
     * Delete an existing account
     * TODO: Should have have a verification with a body request (AccountRequest)?
     */
    @DeleteMapping("/account/{id}")
    fun deleteAccount(
        @PathVariable("id") id: String,
    ): ResponseEntity<AccountResponse?> {
        return accountService.deleteAccount(id).mapBody { it?.toAccountResponse() }
    }
}
