package com.meal.repositories

import com.meal.model.UserRegistration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRegistrationRepository: JpaRepository<UserRegistration, String>