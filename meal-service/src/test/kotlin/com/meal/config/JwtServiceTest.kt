package com.meal.config

import com.meal.users.model.User
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtServiceTest(
    @Autowired val jwtService: JwtService
): ShouldSpec({

    should("extract username") {
        jwtService.extractUsername(JWT) shouldBe USERNAME
    }

    should("generate token for username") {
        val token = jwtService.generateToken(user)

        token.shouldContain("eyJzdWIiOiJEZXgiLCJpYXQi")
    }

    should("generate valid token") {
        val token = jwtService.generateToken(user)

        jwtService.isTokenValid(token, user).shouldBeTrue()
    }

}) {
    override fun extensions() = listOf(SpringExtension)

    companion object {
        private const val JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJEZXgiLCJpYXQiOjE1MTYyMzkwMjIsImV4cCI6OTk5OTk5OTk5OX0.Pq2dzwBIrzPaodZpbnN7Kga0AWeb3ofYTLFL7xEYg8k"
        private const val USERNAME = "Dex"
        private const val PASSWORD = "dog"
        private val user = User(username = USERNAME, password = PASSWORD)
    }
}