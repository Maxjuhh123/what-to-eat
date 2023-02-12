package com.meal.authentication

import com.meal.authentication.model.AuthenticationResponse
import com.meal.authentication.model.UserRegistrationRequest
import com.meal.config.JwtService
import com.meal.users.UserService
import com.meal.users.model.User
import com.meal.users.model.UsernameTakenException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder

class AuthenticationServiceTest: ShouldSpec({

    lateinit var userService: UserService
    lateinit var passwordEncoder: PasswordEncoder
    lateinit var jwtService: JwtService
    lateinit var authenticationManager: AuthenticationManager
    lateinit var authenticationService: AuthenticationService

    beforeEach {
        userService = mockk()
        passwordEncoder = mockk()
        jwtService = mockk()
        authenticationManager = mockk()
        authenticationService = AuthenticationService(userService, passwordEncoder, jwtService, authenticationManager)
    }

    should("register user") {
        val newPassword = "encoded"
        val user = User(username = USERNAME, password = newPassword)
        every { passwordEncoder.encode(PASSWORD) } returns newPassword
        every { userService.addUser(USERNAME, newPassword) } returns user
        every { jwtService.generateToken(user, emptyMap()) } returns JWT
        val request = UserRegistrationRequest(USERNAME, PASSWORD)

        val result = authenticationService.register(request)

        result shouldBe AuthenticationResponse(JWT)
    }

    should("not register user with taken username") {
        val newPassword = "encoded"
        every { passwordEncoder.encode(PASSWORD) } returns newPassword
        every { userService.addUser(USERNAME, newPassword) } throws UsernameTakenException()
        val request = UserRegistrationRequest(USERNAME, PASSWORD)

        shouldThrow<UsernameTakenException> { authenticationService.register(request) }
    }

    should("authenticate user and generate token") {
        val request = UserRegistrationRequest(USERNAME, PASSWORD)
        every { authenticationManager.authenticate(UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)) } returns null
        every { userService.loadUserByUsername(USERNAME) } returns user
        every { jwtService.generateToken(user, emptyMap()) } returns JWT

        val result = authenticationService.authenticate(request)

        result shouldBe AuthenticationResponse(JWT)
        verify(exactly = 1) { authenticationManager.authenticate(UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)) }
        verify(exactly = 1) { userService.loadUserByUsername(USERNAME) }
        verify(exactly = 1) { jwtService.generateToken(user, emptyMap()) }
    }

}) {
    override fun extensions() = listOf(SpringExtension)

    companion object {
        private const val USERNAME = "John"
        private const val PASSWORD = "passcode"
        private const val JWT = "test-token"
        private val user = User(username = USERNAME, password = PASSWORD)
    }
}