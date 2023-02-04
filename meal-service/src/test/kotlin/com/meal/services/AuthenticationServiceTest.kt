package com.meal.services

import com.meal.exceptions.UsernameTakenException
import com.meal.model.UserRegistration
import com.meal.repositories.UserRegistrationRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Optional

class AuthenticationServiceTest: ShouldSpec({

    lateinit var userRegistrationRepository: UserRegistrationRepository
    lateinit var authenticationService: AuthenticationService

    beforeEach {
        userRegistrationRepository = mockk()
        authenticationService = AuthenticationService(userRegistrationRepository)
    }

    should("register user if not exists") {
        val registration = UserRegistration(USERNAME, PASSWORD)
        every { userRegistrationRepository.findById(USERNAME) } returns Optional.empty()
        every { userRegistrationRepository.saveAndFlush(registration) } returns registration

        authenticationService.register(registration)

        verify(exactly = 1) { userRegistrationRepository.saveAndFlush(registration) }
    }

    should("not register user when username exists already") {
        val registration = UserRegistration(USERNAME, PASSWORD)
        every { userRegistrationRepository.findById(USERNAME) } returns Optional.of(registration)

        shouldThrow<UsernameTakenException> { authenticationService.register(registration) }
        verify(exactly = 0) { userRegistrationRepository.saveAndFlush(registration) }
    }

}) {
    companion object {
        private const val USERNAME = "MDG123"
        private const val PASSWORD = "passcode"
    }
}