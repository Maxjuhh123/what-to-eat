package com.meal.services

import com.meal.exceptions.UsernameTakenException
import com.meal.model.UserRegistration
import com.meal.repositories.UserRegistrationRepository
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userRegistrationRepository: UserRegistrationRepository
) {

    fun register(request: UserRegistration) {
        if(userExists(request.username)) {
            throw UsernameTakenException()
        }
        userRegistrationRepository.saveAndFlush(request)
    }

    private fun userExists(username: String) =
        userRegistrationRepository.findById(username).isPresent

}