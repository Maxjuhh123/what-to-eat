package com.meal.services

import com.meal.exceptions.UserNotFoundException
import com.meal.exceptions.UserUnauthorizedException
import com.meal.exceptions.UsernameTakenException
import com.meal.model.UserData
import com.meal.model.UserRegistration
import com.meal.repositories.UserRegistrationRepository
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userRegistrationRepository: UserRegistrationRepository
) {

    /**
     * Register a user with a username and password.
     *
     * @param request - the request containing the username and password.
     * @throws UsernameTakenException if the username has already been taken.
     */
    fun register(request: UserRegistration) {
        if(userExists(request.username)) {
            throw UsernameTakenException()
        }
        userRegistrationRepository.saveAndFlush(request)
    }

    /**
     * Authenticate a user with a username and password, giving their user data
     *
     * @param request - contains the username and password
     * @return the user data found
     * @throws UserNotFoundException if a user with the given username does not exist
     * @throws UserUnauthorizedException if the password is wrong
     */
    fun authenticate(request: UserRegistration): UserData {
        if(!passwordMatches(request)) {
            throw UserUnauthorizedException()
        }
        return userRegistrationRepository.findById(request.username).get().toUserData()
    }

    /**
     * Check if a user with a specific username exists.
     *
     * @param username - the username to check for
     * @return true iff the user exists
     */
    private fun userExists(username: String) =
        userRegistrationRepository.findById(username).isPresent

    /**
     * Check whether the password in the request matches the one stored in the database for the username
     * in the request.
     *
     * @return true iff the password matches
     * @throws UserNotFoundException if there is no user with the given username.
     */
    private fun passwordMatches(request: UserRegistration) =
        userRegistrationRepository
            .findById(request.username)
            .orElseThrow { UserNotFoundException() }
            .password == request.password

}