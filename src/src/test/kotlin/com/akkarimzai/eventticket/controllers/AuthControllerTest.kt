package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.models.user.LoginCommand
import com.akkarimzai.eventticket.models.user.RegisterCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest : AbstractIntegrationTest() {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun `should register new user successfully`() {
        val request = RegisterCommand(
            name = "John Doe",
            email = "Tq4lM@example.com",
            username = "johndoe",
            password = "password"
        )
        webTestClient.post()
            .uri("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.token").isNotEmpty
    }

    @Test
    fun `invalid credentials should return bad request`() {
        val request = RegisterCommand(
            name = "",
            email = "talwak@mail.ru",
            username = "quiswardak",
            password = "majnonquis"
        )
        webTestClient.post()
            .uri("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should return conflict while user with same credentials are already exist`() {
        val request = RegisterCommand(
            name = "John Doe",
            email = "Tq4lM@example.com",
            username = "johndoe",
            password = "password"
        )
        webTestClient.post()
            .uri("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.token").isNotEmpty

        webTestClient.post()
            .uri("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT)
    }

    @Test
    fun `should register and login user successfully`() {
        val request = RegisterCommand(
            name = "John Doe",
            email = "Tq4lM1@example.com",
            username = "johndoe1",
            password = "password"
        )
        webTestClient.post()
            .uri("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.token").isNotEmpty

        webTestClient.post()
            .uri("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.token").isNotEmpty
    }

    @Test
    fun `login for non-existing user should return not found`() {
        val request = LoginCommand(
            username = "invalid username",
            password = "invalid password"
        )
        webTestClient.post()
            .uri("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isNotFound
    }
}
