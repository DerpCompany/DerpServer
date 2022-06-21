package com.derpcompany.server

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.server.ResponseStatusException

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/15/2022 11:18
 */

@Controller
class LoginController(private val repository: ArticleRepository, private val properties: LoginProperties) {

    @GetMapping("/")
    fun login(model: Model): String {
        // add title attribute
        model["title"] = properties.title
        model["banner"] = properties.banner
        model["articles"] = repository.findAllByOrderByAddedAtDesc().map { it.render() }
        return "login"
    }

    @GetMapping("/article/{slug}")
    fun article(@PathVariable slug: String, model: Model): String {
        val article = repository
            .findBySlug(slug)
            ?.render()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This article doesn't exist")
        model["title"] = article.title
        model["article"] = article
        return "article"
    }

    // rendering the articles attributes
    fun Article.render() = RenderedArticle(
        slug,
        title,
        headline,
        content,
        author,
        addedAt.format()
    )

    // Article class declaration
    data class RenderedArticle(
        val slug: String,
        val title: String,
        val headline: String,
        val content: String,
        val author: Author,
        val addedAt: String
    )
}