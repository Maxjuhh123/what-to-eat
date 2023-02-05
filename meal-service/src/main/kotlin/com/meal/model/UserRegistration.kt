package com.meal.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "registrations")
data class UserRegistration(
    @Id
    @Column(name = "username")
    val username: String = "",
    @Column(name = "password")
    val password: String = ""
)