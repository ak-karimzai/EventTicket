package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.Role
import com.akkarimzai.eventticket.entities.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.springframework.security.core.userdetails.UserDetails

class JwtServiceTest : FunSpec({
    val mockJwtKey = "c63a4b9c304df32b788ca2404869a51b44adaa1d253cf9f5eb5bd75b45e5aea9"
    val mockTtl: Long = 3600000
    val jwtService = spyk(JwtService(mockJwtKey, mockTtl))

    test("generateToken should create a valid JWT token") {
        // Arrange
        val user = User(id = 1L, name = "testtest", email = "email@mail.ru",
            phoneNumber = null, "username", "password", role = Role.USER)
        val token = jwtService.generateToken(user)

        // Act && Assert
        token shouldNotBe null
        token.startsWith("eyJ") shouldBe true
    }

    test("extractUsername should return the correct username from the token") {
        // Arrange
        val user = User(id = 1L, name = "testtest", email = "email@mail.ru",
            phoneNumber = null, "username", "password", role = Role.USER)
        val token = jwtService.generateToken(user)

        // Act
        val username = jwtService.extractUsername(token)

        // Assert
        username shouldBe "username"
    }

    test("isTokenExpired should return true if the token is expired") {
        // Arrange
        val expiredToken = "invalid-token"
        every { jwtService.isTokenExpired(expiredToken) } returns true

        // Act
        val result = jwtService.isTokenExpired(expiredToken)

        // Assert
        result shouldBe true
    }

    test("isTokenExpired should return false if the token is not expired") {
        // Arrange
        val validToken = "Valid-token"
        every { jwtService.isTokenExpired(validToken) } returns false

        // Act
        val result = jwtService.isTokenExpired(validToken)

        // Assert
        result shouldBe false
    }

    test("isTokenValid should return true for a valid token") {
        // Arrange
        val validToken = "Valid-token"
        every { jwtService.isTokenValid(validToken) } returns true

        // Act
        val result = jwtService.isTokenValid(validToken)

        // Assert
        result shouldBe true
    }

    test("isTokenValid should return false for an invalid token") {
        // Arrange
        val invalidToken = "invalid-token"
        every { jwtService.isTokenValid(invalidToken) } returns false

        // Act
        val result = jwtService.isTokenValid(invalidToken)

        // Assert
        result shouldBe false
    }
})
