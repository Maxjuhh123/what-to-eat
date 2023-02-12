package com.meal.authentication

import com.meal.authentication.model.AuthenticationResponse
import com.meal.authentication.model.UserRegistrationRequest
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    /**
     * Register a user with a username and password.
     *
     * @param request - The request containing the username and password
     * @returns - 200 if registered successfully
     *          - 403 when the username is already taken
     */
    @PostMapping("/register")
    fun register(@RequestBody request: UserRegistrationRequest): ResponseEntity<AuthenticationResponse> =
        ok(authenticationService.register(request))

    /**
     * Register a user with a username and password.
     *
     * @param request - The request containing the username and password
     * @returns - 200 if registered successfully
     *          - 403 when the username is already taken
     */
    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: UserRegistrationRequest): ResponseEntity<AuthenticationResponse> =
        ok(authenticationService.authenticate(request))

}