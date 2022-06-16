package com.derpcompany.server

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/15/2022 12:11
 */

@Entity
class Article(
    var title: String,
    var headline: String,
    var content: String,
    @ManyToOne var author: User,
    var slug: String = title.toSlug(),
    var addedAt: LocalDateTime = LocalDateTime.now(),
    @Id @GeneratedValue var id: Long? = null
)

@Entity
class User(
    var username: String,
    var email: String,
    var role: String, //Todo: later add enum and @OneToOne
    var linkedAccount: String?, // Todo: later make a list of SSOIDs, @OneToMany
    @Id @GeneratedValue var userId: Long? = null
)