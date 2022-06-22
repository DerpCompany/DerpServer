package com.example.mongodb_server.controllers

import com.example.mongodb_server.entities.SavedUser

data class ResponseUser(
    val userId: String,
    val username: String,
    val email: String,
    val role: String, // TODO: convert to ENUM
    val createdDate: Long,
    val modifiedDate: Long,
)

fun SavedUser.toResponseUsers(): ResponseUser {
    return ResponseUser(
        userId = userId.toHexString(),
        username,
        email,
        role,
        createdDate = 0L,
        modifiedDate = 0L
    )
}