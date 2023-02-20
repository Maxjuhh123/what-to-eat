package com.meal.util

import com.meal.users.model.User
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.core.Authentication

class AuthUtilTest: ShouldSpec({

    should("extract userId from authentication") {
        val authentication: Authentication = mockk()
        every { authentication.principal } returns User(USER_ID, "name", "password")

        val result = AuthUtil.extractUserId(authentication)

        result shouldBe USER_ID
    }

}) {
    companion object {
        private const val USER_ID = 432324L
    }
}