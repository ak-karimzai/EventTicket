package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.exceptions.ConflictException
import com.akkarimzai.eventticket.exceptions.UnauthorizedException
import com.akkarimzai.eventticket.models.user.AuthResponse
import com.akkarimzai.eventticket.models.user.LoginCommand
import com.akkarimzai.eventticket.models.user.RegisterCommand
import com.akkarimzai.eventticket.services.UserService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder

class AuthServiceImplTest : FunSpec({
    val userService = mockk<UserService>()
    val jwtService = mockk<JwtService>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val authService = AuthServiceImpl(userService, jwtService, passwordEncoder)

    beforeTest {
    }

    test("register should create a new user and return an AuthResponse") {
        // Arrange
        val command = RegisterCommand(
            name = "testtest",
            email = "quis@mail.ru",
            null,
            username = "testuser",
            password = "password123"
        )
        val encodedPassword = "encodedPassword123"
        val user = User(
            id = 1,
            name = command.name,
            email = command.email,
            phoneNumber = command.phoneNumber,
            username = command.username,
            password = encodedPassword
        )
        val authResponse = AuthResponse(token = "jwt-token")

        every { passwordEncoder.encode(command.password) } returns encodedPassword
        every { userService.create(any()) } returns user
        every { jwtService.generateToken(any()) } returns authResponse.token

        // Act
        val result = authService.register(command)

        // Assert
        result shouldBe authResponse
        verify { passwordEncoder.encode(command.password) }
        verify { userService.create(any()) }
        verify { jwtService.generateToken(any()) }
    }

    test("login should throw UnauthorizedException for invalid password") {
        // Arrange
        val command = LoginCommand(username = "testuser", password = "wrongpassword")
        val user = User(id = 1, username = "testuser", password = "encodedPassword123",
            name = "", email = "")

        every { userService.loadByUsername(command.username) } returns user
        every { passwordEncoder.matches(command.password, user.password) } returns false

        // Act && Assert
        shouldThrow<UnauthorizedException> {
            authService.login(command)
        }

        verify { userService.loadByUsername(command.username) }
        verify { passwordEncoder.matches(command.password, user.password) }
    }

    test("register should handle duplicate user exception") {
        // Arrange
        val command = RegisterCommand(
            name = "testtest", email = "quis@mail.ru", phoneNumber = null,
            username = "testuser", password = "password123"
        )
        every { passwordEncoder.encode(command.password) } returns "encodedPassword123"
        every { userService.create(any()) } throws ConflictException("user", command.username)

        // Act && Assert
        shouldThrow<ConflictException> {
            authService.register(command)
        }

        verify { passwordEncoder.encode(command.password) }
        verify { userService.create(any()) }
    }

    test("currentUser should return authenticated user") {
        // Arrange
        val username = "testuser"
        val user = User(id = 1, username = "testuser", password = "encodedPassword123",
            name = "", email = "")

        mockkStatic(SecurityContextHolder::class)
        val authentication = mockk<Authentication>()
        every { SecurityContextHolder.getContext().authentication } returns authentication
        every { authentication.name } returns username
        every { userService.loadByUsername(username) } returns user

        // Act
        val result = authService.currentUser()

        // Assert
        result shouldBe user

        verify { userService.loadByUsername(username) }
        verify { SecurityContextHolder.getContext().authentication }
    }
})
