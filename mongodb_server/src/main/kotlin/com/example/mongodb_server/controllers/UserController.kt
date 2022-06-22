package com.example.mongodb_server.controllers

import com.example.mongodb_server.entities.SavedUser
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
    fun getAllUsers(): ResponseEntity<List<ResponseUser>> {
        val users = userRepository.findAll().map { it.toResponseUsers() }
        return ResponseEntity.ok(users)
    }

    @GetMapping("/id/{id}")
    fun getOneUserByUserId(@PathVariable("id") id: String): ResponseEntity<ResponseUser> {
        val user = userRepository.findOneByUserId(ObjectId(id)).toResponseUsers()
        return ResponseEntity.ok(user)
    }

    @GetMapping("/username/{username}")
    fun getOneUserByUsername(@PathVariable("username") username: String): ResponseEntity<ResponseUser> {
        val user = userRepository.findByUsername(username).toResponseUsers()
        return ResponseEntity.ok(user)
    }

    @GetMapping("/roles/{role}")
    fun getUsersByRoles(@PathVariable("role") role: String): ResponseEntity<List<ResponseUser>> {
        val users = userRepository.findByRole(role).map { it.toResponseUsers() }
        return ResponseEntity.ok(users)
    }
}