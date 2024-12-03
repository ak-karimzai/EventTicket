package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.models.category.CreateCategoryCommand
import com.akkarimzai.eventticket.models.category.UpdateCategoryCommand
import com.akkarimzai.eventticket.models.event.CreateEventCommand
import com.akkarimzai.eventticket.models.ticket.CreateTicketCommand
import com.akkarimzai.eventticket.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDateTime
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val passwordEncoder: PasswordEncoder
): AuthorizedIntegrationTest() {
    @BeforeEach
    fun `refresh tokens`() {
        renewTokens(userRepository, webTestClient, passwordEncoder)
    }

    @Test
    fun `should create and load category`() {
        val request = CreateCategoryCommand(title = "test category")

        webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .value { categoryId ->
                webTestClient.get()
                    .uri("/api/v1/categories/$categoryId")
                    .exchange().expectStatus().isOk
                    .expectBody()
                    .jsonPath("$.title").isEqualTo(request.title)
            }
    }

    @Test
    fun `should create and load category embedded with events and tickets`() {
        val request = CreateCategoryCommand(title = "test category",
            events = listOf(
                CreateEventCommand(
                    title = "test event",
                    artist = "test artist",
                    date = LocalDateTime.now().plusDays(7),
                    tickets = listOf(CreateTicketCommand(
                        title = "test ticket",
                        description = "test description",
                        price = 100.0
                    )))))

        webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .value { categoryId ->
                webTestClient.get()
                    .uri("/api/v1/categories/$categoryId")
                    .exchange().expectStatus().isOk
                    .expectBody()
                    .jsonPath("$.title").isEqualTo(request.title)
            }
    }

    @Test
    fun `update category should update the title`() {
        val request = CreateCategoryCommand(title = "test category",
            events = listOf(
                CreateEventCommand(
                    title = "test event",
                    artist = "test artist",
                    date = LocalDateTime.now().plusDays(7),
                    tickets = listOf(CreateTicketCommand(
                        title = "test ticket",
                        description = "test description",
                        price = 100.0
                    )))))
        val updateRequest = UpdateCategoryCommand(title = "test category updated")

        webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .value { categoryId ->
                webTestClient.put()
                    .uri("/api/v1/categories/$categoryId")
                    .header("Authorization", "Bearer $adminToken")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateRequest)
                    .exchange().expectStatus().isNoContent
            }
    }
}