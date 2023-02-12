package com.meal.users

import com.meal.users.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository: JpaRepository<User, String> {

    fun findByUsername(username: String): Optional<User>

}