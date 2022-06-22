package com.example.mongodb_server.controllers

import com.example.mongodb_server.controllers.data.NewAccount
import com.example.mongodb_server.controllers.data.ResponseUser
import com.example.mongodb_server.controllers.data.toResponseUser
import com.example.mongodb_server.repositories.entities.SavedUser
import com.example.mongodb_server.repositories.UserRepository
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
class UserController(private val userRepository: UserRepository) {

    /**
     * Query all site users
     */
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<ResponseUser>> {
        val users = userRepository.findAll().map { it.toResponseUser() }
        return ResponseEntity.ok(users)
    }

    /**
     * Query user by ID
     */
    @GetMapping("/id/{id}")
    fun getOneUserByUserId(@PathVariable("id") id: String): ResponseEntity<ResponseUser> {
        val user = userRepository.findOneByUserId(ObjectId(id)).toResponseUser()
        return ResponseEntity.ok(user)
    }

    /**
     * Query user by username
     */
    @GetMapping("/username/{username}")
    fun getOneUserByUsername(@PathVariable("username") username: String): ResponseEntity<ResponseUser> {
        val user = userRepository.findByUsername(username).toResponseUser()
        return ResponseEntity.ok(user)
    }

    /**
     * Query all users with specific role
     */
    @GetMapping("/roles/{role}")
    fun getUsersByRoles(@PathVariable("role") role: String): ResponseEntity<List<ResponseUser>> {
        val users = userRepository.findByRole(role).map { it.toResponseUser() }
        return ResponseEntity.ok(users)
    }

    /**
     * Create new user account
     */
    @PostMapping
    fun createAccount(@RequestBody request: NewAccount): ResponseEntity<ResponseUser> {
        val newUser = (SavedUser(
            userId = ObjectId(),
            username = request.username,
            email = request.email,
            role = "unapproved",
            createdDate = LocalDateTime.now(),
            modifiedDate = LocalDateTime.now(),
        ))

        userRepository.save(newUser)
        return ResponseEntity(newUser.toResponseUser(), HttpStatus.CREATED)
    }

    /**
     * Update an existing account
     */
    @PutMapping("/{id}")
    fun updateAccount(@RequestBody request: NewAccount, @PathVariable("id") id: String): ResponseEntity<ResponseUser> {
        val user = userRepository.findOneByUserId(ObjectId(id))

        val updatedUser = (SavedUser(
            userId = user.userId,
            username = request.username,
            email = request.email,
            role = user.role,
            createdDate = user.createdDate,
            modifiedDate = LocalDateTime.now(),
        ))

        userRepository.save(updatedUser)
        return ResponseEntity.ok(updatedUser.toResponseUser())
    }

    /**
     * Delete an existing account
     */
    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable("id") id: String): ResponseEntity<ResponseUser> {
        userRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}