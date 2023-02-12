package com.meal.users

import com.meal.users.model.User
import com.meal.users.model.UsernameTakenException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
): UserDetailsService {

    /**
     * Add a user to the database.
     *
     * @param username - username of the user to add
     * @param password - password of the user to add
     * @return the added user
     */
    fun addUser(username: String, password: String): User {
        if(existsByUsername(username)) {
            throw UsernameTakenException()
        }
        return userRepository.saveAndFlush(User(username = username, password = password))
    }

    /**
     * Get a user by username.
     *
     * @param username - the username to find a user for
     * @throws UsernameNotFoundException if the username is null or not found
     */
    override fun loadUserByUsername(username: String?): UserDetails {
        if(username == null) {
            throw UsernameNotFoundException("No username given")
        }
        return userRepository.findByUsername(username).orElseThrow { UsernameNotFoundException("User with username not found") }
    }

    /**
     * Check whether a user with a specific username exists.
     *
     * @param username - the username to check for
     * @return true iff a user with the given username exists
     */
    private fun existsByUsername(username: String) =
        userRepository.findByUsername(username).isPresent

}