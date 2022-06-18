package com.derpcompany.server

import com.derpcompany.server.entities.User
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
) {
    @Test
    fun `When findByIdOrNull then return User`() {
        val empathyAwaits = User(
            "empathyAwaits", "garcia.alcia1990@gmail.com", "admin"
        )
        entityManager.persist(empathyAwaits)
        entityManager.flush()

        val found = userRepository.findByUserId(empathyAwaits.username)
        assertThat(found).isEqualTo(empathyAwaits)
    }

    @Test
    fun `When findByLogin then return User`() {
        val empathyAwaits = User(
            "empathyAwaits", "garcia.alcia1990@gmail.com", "admin"
        )
        entityManager.persist(empathyAwaits)
        entityManager.flush()

        val user = userRepository.findById(empathyAwaits.username)
        assertThat(user).isEqualTo(empathyAwaits)
    }
}