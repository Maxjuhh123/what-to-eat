package com.meal.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import com.meal.MealApplication
import com.meal.authentication.model.AuthenticationResponse
import com.meal.authentication.model.UserRegistrationRequest
import com.meal.users.model.UsernameTakenException
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest(classes = [MealApplication::class])
@AutoConfigureMockMvc
class AuthenticationControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @MockkBean private val authenticationService: AuthenticationService,
): ShouldSpec ({

    val mapper = ObjectMapper()

    should("register user") {
        val request = UserRegistrationRequest(USERNAME, PASSWORD)
        val expected = AuthenticationResponse(JWT)
        every { authenticationService.register(request) } returns expected
        val result = mockMvc.post("/users/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }
            .andReturn().response

        result.status shouldBe 200
        result.contentAsString shouldBe mapper.writeValueAsString(expected)
        verify(exactly = 1) { authenticationService.register(request) }
    }

    should("not register user with taken username") {
        val request = UserRegistrationRequest(USERNAME, PASSWORD)
        every { authenticationService.register(request) } throws UsernameTakenException()
        val result = mockMvc.post("/users/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }
            .andReturn().response

        result.status shouldBe 403
        result.contentAsString shouldBe ""
        verify(exactly = 1) { authenticationService.register(request) }
    }

    should("authenticate user with correct username/password combination") {
        val request = UserRegistrationRequest(USERNAME, PASSWORD)
        val expected = AuthenticationResponse(JWT)
        every { authenticationService.authenticate(request) } returns expected

        val result = mockMvc.post("/users/auth/authenticate") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }
            .andReturn().response

        result.status shouldBe 200
        result.contentAsString shouldBe mapper.writeValueAsString(expected)
        verify(exactly = 1) { authenticationService.authenticate(request) }
    }

    should("not authenticate user with wrong credentials") {
        val request = UserRegistrationRequest(USERNAME, PASSWORD)
        every { authenticationService.authenticate(request) } throws BadCredentialsException("wrong credentials")

        val result = mockMvc.post("/users/auth/authenticate") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }
            .andReturn().response

        result.status shouldBe 403
        verify(exactly = 1) { authenticationService.authenticate(request) }
    }

    should("not authenticate with non-existent username") {
        val request = UserRegistrationRequest(USERNAME, PASSWORD)
        every { authenticationService.authenticate(request) } throws UsernameNotFoundException("Username not found")

        val result = mockMvc.post("/users/auth/authenticate") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }
            .andReturn().response

        result.status shouldBe 403
        verify(exactly = 1) { authenticationService.authenticate(request) }
    }

}) {
    override fun isolationMode() = IsolationMode.InstancePerTest

    override fun extensions() = listOf(SpringExtension)

    companion object {
        private const val USERNAME = "MDG123"
        private const val PASSWORD = "passcode"
        private const val JWT = "test-token"
    }
}