package com.meal.util

import com.meal.users.model.User
import org.springframework.security.core.Authentication

object AuthUtil {

    /**
     * Extract userId from authentication given by JWT, assuming it contains one
     *
     * @param authentication - the authentication containing the user info
     * @return the userId found
     */
    fun extractUserId(authentication: Authentication): Long =
        (authentication.principal as User).userId!!

}