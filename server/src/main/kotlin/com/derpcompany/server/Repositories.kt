package com.derpcompany.server

import org.springframework.data.repository.CrudRepository

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/15/2022 12:19
 */

interface ArticleRepository : CrudRepository<Article, Long> {
    fun findBySlug(slug: String): Article?
    fun findAllByOrderByAddedAtDesc(): Iterable<Article>
}

interface AuthorRepository : CrudRepository<Author, Long> {
    fun findByUsername(username: String): Author?
}