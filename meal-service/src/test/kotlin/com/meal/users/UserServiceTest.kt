package com.meal.users

import com.meal.users.model.User
import com.meal.users.model.UsernameTakenException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.Optional

class UserServiceTest: ShouldSpec ({

    lateinit var userRepository: UserRepository
    lateinit var userService: UserService

    beforeEach {
        userRepository = mockk()
        userService = UserService(userRepository)
    }

    should("add a user") {
        every { userRepository.findByUsername(USERNAME) } returns Optional.empty()
        every { userRepository.saveAndFlush(user) } returns user

        val result = userService.addUser(USERNAME, PASSWORD)

        result shouldBe user
        verify(exactly = 1) { userRepository.findByUsername(USERNAME) }
        verify(exactly = 1) { userRepository.saveAndFlush(user) }
    }

    should("not add a user if username already exists") {
        every { userRepository.findByUsername(USERNAME) } returns Optional.of(user)

        shouldThrow<UsernameTakenException> { userService.addUser(USERNAME, PASSWORD) }
        verify(exactly = 1) { userRepository.findByUsername(USERNAME) }
        verify(exactly = 0) { userRepository.saveAndFlush(user) }
    }

    should("load user by username") {
        every { userRepository.findByUsername(USERNAME) } returns Optional.of(user)

        val result = userService.loadUserByUsername(USERNAME)

        result shouldBe user
        verify(exactly = 1) { userRepository.findByUsername(USERNAME) }
    }

    should("not load user by username if username is null") {
        shouldThrow<UsernameNotFoundException> { userService.loadUserByUsername(null) }
        verify(exactly = 0) { userRepository.findByUsername(USERNAME) }
    }

    should("not load user by username if username does not exist") {
        every { userRepository.findByUsername(USERNAME) } returns Optional.empty()

        shouldThrow<UsernameNotFoundException> { userService.loadUserByUsername(USERNAME) }
        verify(exactly = 1) { userRepository.findByUsername(USERNAME) }
    }

}) {
    override fun extensions() = listOf(SpringExtension)

    override fun isolationMode() = IsolationMode.InstancePerTest

    companion object {
        private const val USERNAME = "user"
        private const val PASSWORD = "passcode"
        private val user = User(username = USERNAME, password = PASSWORD)
    }
}