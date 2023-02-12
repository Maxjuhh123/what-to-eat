package com.meal.authentication

import com.meal.authentication.model.AuthenticationResponse
import com.meal.authentication.model.UserRegistrationRequest
import com.meal.config.JwtService
import com.meal.users.UserService
import com.meal.users.model.UsernameTakenException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {

    /**
     * Register a user with a username and password.
     * Gives an authentication token (JWT)
     *
     * @param request - the request containing the username and password.
     * @throws UsernameTakenException if the username has already been taken.
     * @return response containing the generated JWT
     */
    fun register(request: UserRegistrationRequest): AuthenticationResponse {
        val user = userService.addUser(request.username, passwordEncoder.encode(request.password))
        val token = jwtService.generateToken(user)
        return AuthenticationResponse(token = token)
    }

    /**
     * Authenticate a user with a username and password.
     * Gives an authentication token (JWT)
     *
     * @param request - the request containing the username and password
     * @throws AuthenticationException when the username/password combination is wrong
     * @throws UsernameNotFoundException when the username is not found
     * @return a response containing the token
     */
    fun authenticate(request: UserRegistrationRequest): AuthenticationResponse {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.username, request.password))
        val user = userService.loadUserByUsername(request.username)
        val token = jwtService.generateToken(user)
        return AuthenticationResponse(token)
    }

}