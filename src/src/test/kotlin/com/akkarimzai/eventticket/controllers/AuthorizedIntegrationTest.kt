package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.entities.Role
import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.models.user.AuthResponse
import com.akkarimzai.eventticket.models.user.LoginCommand
import com.akkarimzai.eventticket.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.reactive.server.WebTestClient

open class AuthorizedIntegrationTest(
    private val userRepository: UserRepository,
    private val webTestClient: WebTestClient,
    private val passwordEncoder: PasswordEncoder
) : AbstractIntegrationTest() {
    protected lateinit var adminToken: String
    protected lateinit var userToken: String

    @BeforeEach
    protected fun renewTokens() {
        val admin = userRepository.findByUsername("adminadmin") ?: User(
            name = "Admin",
            email = "admin@admin.com",
            username = "adminadmin",
            password = passwordEncoder.encode("adminpassword"),
            role = Role.ADMIN
        ).also { userRepository.save(it) }

        val user = userRepository.findByUsername("useruser") ?: User(
            name = "User",
            email = "user@user.com",
            username = "useruser",
            password = passwordEncoder.encode("userpassword"),
            role = Role.USER
        ).also { userRepository.save(it) }

        val adminRequest = LoginCommand(username = admin.username, password = "adminpassword")
        val userRequest = LoginCommand(username = user.username, password = "userpassword")

        adminToken = retrieveToken(adminRequest, webTestClient)
        userToken = retrieveToken(userRequest, webTestClient)
    }

    private fun retrieveToken(request: LoginCommand, webTestClient: WebTestClient): String {
        return webTestClient.post()
            .uri("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody(AuthResponse::class.java)
            .returnResult()
            .responseBody!!.token
    }
}