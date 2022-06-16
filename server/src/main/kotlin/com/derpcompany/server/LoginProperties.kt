package com.derpcompany.server

import org.springframework.boot.Banner
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/15/2022 16:48
 */

@ConstructorBinding
@ConfigurationProperties("login")
data class LoginProperties(var title: String, val banner: Banner) {
    data class Banner(val title: String? = null, val content: String)
}
