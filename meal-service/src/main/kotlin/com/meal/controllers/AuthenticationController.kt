package com.meal.controllers

import com.meal.model.UserRegistration
import com.meal.services.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
internal class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: UserRegistration): ResponseEntity<Any> {
        authenticationService.register(request)
        return ok().build()
    }

}