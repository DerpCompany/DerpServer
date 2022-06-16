package com.derpcompany.server

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

/**
 * Author: garci
 * Version: 1.0
 * Date: 6/15/2022 12:21
 */

@DataJpaTest
class RepositoriesTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository,
    val articleRepository: ArticleRepository
) {
    @Test
    fun `When findByIdOrNull then return Article`() {
        val empathyAwaits = User(
            "empathyAwaits", "garcia.alcia1990@gmail.com", "admin", "Discord"
        )
        entityManager.persist(empathyAwaits)

        val article = Article(
            "Spring Framework 5.0 goes GA", "Dear Spring community ...", "Lorem ipsum", empathyAwaits
        )
        entityManager.persist(article)
        entityManager.flush()

        val found = articleRepository.findByIdOrNull(article.id!!)
        assertThat(found).isEqualTo(article)
    }

    @Test
    fun `When findByLogin then return User`() {
        val empathyAwaits = User(
            "empathyAwaits", "garcia.alcia1990@gmail.com", "admin", "Discord"
        )
        entityManager.persist(empathyAwaits)
        entityManager.flush()

        val user = userRepository.findByUsername(empathyAwaits.username)
        assertThat(user).isEqualTo(empathyAwaits)
    }
}