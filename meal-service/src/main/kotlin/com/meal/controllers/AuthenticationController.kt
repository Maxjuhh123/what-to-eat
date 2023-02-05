package com.meal.controllers

import com.meal.model.UserData
import com.meal.model.UserRegistration
import com.meal.services.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
internal class AuthenticationController(
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
    fun register(@RequestBody request: UserRegistration): ResponseEntity<Any> {
        authenticationService.register(request)
        return ok().build()
    }

    /**
     * Authenticate a user to get their user data.
     *
     * @param request - The request containing the username and password
     * @returns - 200 with user data if username and password combination exists
     *          - 404 if user with that username does not exist
     *          - 401 if the password is incorrect
     */
    @GetMapping("/authenticate")
    fun authenticate(@RequestBody request: UserRegistration): ResponseEntity<UserData> =
        ok(authenticationService.authenticate(request))

}