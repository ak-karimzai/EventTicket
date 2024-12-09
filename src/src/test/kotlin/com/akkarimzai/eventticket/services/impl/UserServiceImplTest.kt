package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.Role
import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.exceptions.ConflictException
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.user.UpdateUserCommand
import com.akkarimzai.eventticket.repositories.UserRepository
import io.jsonwebtoken.security.Password
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceImplTest : FunSpec({
    val mockRepository = mockk<UserRepository>()
    val userService = UserServiceImpl(mockRepository)

    test("loadByUsername should return user when found") {
        // Arrange
        val username = "testuser"
        val user = User(id = 1, name = "testtest", email = "test@example.com",
            username = username, password = "testtest", role = Role.USER)

        every { mockRepository.findByUsername(username) } returns user

        // Act && Assert
        userService.loadByUsername(username) shouldBe user
    }

    test("loadByUsername should throw NotFoundException when user not found") {
        // Arrange
        val username = "nonexistentuser"

        every { mockRepository.findByUsername(username) } returns null

        // Act && Assert
        shouldThrow<NotFoundException> { userService.loadByUsername(username) }
    }

    test("create should save user when username and email do not exist") {
        // Arrange
        val user = User(id = 1, name = "testtest", email = "test@example.com",
            username = "testuser", password = "testtest", role = Role.USER)

        every { mockRepository.existsByUsername(user.username) } returns false
        every { mockRepository.existsByEmail(user.email) } returns false
        every { mockRepository.save(user) } returns user

        // Act
        val createdUser = userService.create(user)

        // Assert
        createdUser shouldBe user
    }

    test("create should throw ConflictException if username exists") {
        // Arrange
        val user = User(id = 1, name = "testtest", email = "test@example.com",
            username = "testuser", password = "testtest", role = Role.USER)

        every { mockRepository.existsByUsername(user.username) } returns true

        // Act && Assert
        shouldThrow<ConflictException> { userService.create(user) }
    }

    test("create should throw ConflictException if email exists") {
        // Arrange
        val user = User(id = 1, name = "testtest", email = "test@example.com",
            username = "testuser", password = "testtest", role = Role.USER)

        every { mockRepository.existsByUsername(user.username) } returns false
        every { mockRepository.existsByEmail(user.email) } returns true

        // Act && Assert
        shouldThrow<ConflictException> { userService.create(user) }
    }

    test("update should call create method") {
        // Arrange
        val user = User(id = 1, name = "testtest", email = "test@example.com",
            username = "testuser", password = "testtest", role = Role.USER)
        val updateRequest = UpdateUserCommand(name = "testtest", null, null, null, null)

        every { mockRepository.existsByUsername(user.username) } returns false
        every { mockRepository.existsByEmail(user.email) } returns false
        every { userService.create(user) } returns user

        // Act
        val updatedUser = userService.update(user)

        // Assert
        updatedUser shouldBe user
    }
})
