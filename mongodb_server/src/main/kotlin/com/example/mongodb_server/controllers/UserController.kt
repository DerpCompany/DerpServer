package com.example.mongodb_server.controllers

import com.example.mongodb_server.entities.User
import com.example.mongodb_server.repositories.UserRepository
import org.bson.types.ObjectId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping ("/api/user")
class UserController(private val userRepository: UserRepository) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        val users = userRepository.findAll()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/id/{id}")
    fun getOneUserById(@PathVariable("id") id: String): ResponseEntity<User> {
        val user = userRepository.findOneById(ObjectId(id))
        return ResponseEntity.ok(user)
    }

    @GetMapping("/username/{username}")
    fun getOneUserByUsername(@PathVariable("usernmae") username: String): ResponseEntity<User> {
        val user = userRepository.findByUsername(username)
        return ResponseEntity.ok(user)
    }

    @GetMapping("/roles/{role}")
    fun getUsersByRoles(@PathVariable("role") role: String): ResponseEntity<List<User>> {
        val users = userRepository.findByRole(role)
        return ResponseEntity.ok(users)
    }
}