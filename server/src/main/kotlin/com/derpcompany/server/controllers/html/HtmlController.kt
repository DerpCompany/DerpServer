package com.derpcompany.server.controllers.html

import com.derpcompany.server.LoginProperties
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/15/2022 11:18
 */

@Controller
class HtmlController(private val properties: LoginProperties) {

    @GetMapping("/")
    fun login(model: Model): String {
        // add title attribute
        model["title"] = properties.title
        model["banner"] = properties.banner
        return "login"
    }
}