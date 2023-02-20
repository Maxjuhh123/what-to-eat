package com.meal.users.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "_users")
data class User(
        @Id
        @GeneratedValue
        val userId: Long? = null,
        @Column(name = "username")
        private val username: String = "",
        @Column(name = "password")
        private val password: String = ""
): UserDetails {

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
                mutableListOf()

        override fun getPassword(): String =
                password

        override fun getUsername(): String =
                username

        override fun isAccountNonExpired(): Boolean =
                true

        override fun isAccountNonLocked(): Boolean =
                true

        override fun isCredentialsNonExpired(): Boolean =
                true

        override fun isEnabled(): Boolean =
                true

}