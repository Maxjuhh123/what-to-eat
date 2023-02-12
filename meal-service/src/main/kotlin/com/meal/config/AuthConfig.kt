package com.meal.config

import com.meal.users.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class AuthConfig(
    private val userService: UserService
) {

    /**
     * Determines which authentication provider to use (user service and password encoder).
     *
     * @return the resulting AuthenticationProvider object
     */
    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val daoAuthProvider = DaoAuthenticationProvider()
        daoAuthProvider.setUserDetailsService(userService)
        daoAuthProvider.setPasswordEncoder(passwordEncoder())
        return daoAuthProvider
    }

    /**
     * Determine which password encoding to use.
     *
     * @return the resulting PasswordEncoder
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()

    /**
     * Configure authentication manager.
     *
     * @return the resulting authentication manager
     */
    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager
}