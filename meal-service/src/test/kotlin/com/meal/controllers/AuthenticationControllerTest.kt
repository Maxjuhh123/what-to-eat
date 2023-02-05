package com.meal.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.meal.exceptions.UserNotFoundException
import com.meal.exceptions.UserUnauthorizedException
import com.meal.exceptions.UsernameTakenException
import com.meal.model.UserData
import com.meal.model.UserRegistration
import com.meal.services.AuthenticationService
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(controllers = [AuthenticationController::class])
@AutoConfigureMockMvc
class AuthenticationControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @MockkBean private val authenticationService: AuthenticationService
): ShouldSpec ({

    val mapper = ObjectMapper()

    should("register user if not exists") {
        val registration = UserRegistration(USERNAME, PASSWORD)
        every {authenticationService.register(registration) } just runs

        val result = mockMvc.post("/users/register") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(registration)
            accept = MediaType.APPLICATION_JSON
        }
            .andReturn().response

        result.status shouldBe 200
        verify(exactly = 1) { authenticationService.register(registration) }
    }

    should("not register user when username is taken") {
        val registration = UserRegistration(USERNAME, PASSWORD)
        every {authenticationService.register(registration) } throws UsernameTakenException()

        val result = mockMvc.post("/users/register") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(registration)
            accept = MediaType.APPLICATION_JSON
        }
            .andReturn().response

        result.status shouldBe HttpStatus.FORBIDDEN.value()
        verify(exactly = 1) { authenticationService.register(registration) }
    }

    should("authenticate with correct password") {
        val request = UserRegistration(USERNAME, PASSWORD)
        val expected = UserData(USERNAME)
        every { authenticationService.authenticate(request) } returns expected

        val result = mockMvc.get("/users/authenticate") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
            accept = MediaType.APPLICATION_JSON
        }
            .andReturn().response

        result.status shouldBe HttpStatus.OK.value()
        result.contentAsString shouldBe mapper.writeValueAsString(expected)
        verify(exactly = 1) { authenticationService.authenticate(request) }
    }

    should("not authenticate with incorrect password") {
        val request = UserRegistration(USERNAME, PASSWORD)
        every { authenticationService.authenticate(request) } throws UserUnauthorizedException()

        val result = mockMvc.get("/users/authenticate") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
            accept = MediaType.APPLICATION_JSON
        }
            .andReturn().response

        result.status shouldBe HttpStatus.UNAUTHORIZED.value()
        verify(exactly = 1) { authenticationService.authenticate(request) }
    }

    should("not authenticate with non-existing username") {
        val request = UserRegistration(USERNAME, PASSWORD)
        every { authenticationService.authenticate(request) } throws UserNotFoundException()

        val result = mockMvc.get("/users/authenticate") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
            accept = MediaType.APPLICATION_JSON
        }
            .andReturn().response

        result.status shouldBe HttpStatus.NOT_FOUND.value()
        verify(exactly = 1) { authenticationService.authenticate(request) }
    }

}) {
    override fun isolationMode() = IsolationMode.InstancePerTest

    override fun extensions() = listOf(SpringExtension)

    companion object {
        private const val USERNAME = "MDG123"
        private const val PASSWORD = "passcode"
    }
}