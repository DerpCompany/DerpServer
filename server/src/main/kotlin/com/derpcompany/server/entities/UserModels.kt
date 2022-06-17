package com.derpcompany.server.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/15/2022 12:11
 */

@Document("users")
data class User(
    @Id
    var username: String,
    var email: String,
    var role: String //Todo: later add enum and @OneToOne
)

@Document("linkedaccounts")
data class LinkedAccounts(
    @Id
    var userId: User,
    var accounts: Map<String, String> // list of SSOIDs from Oauth2 linked accounts with their keys(names)
)