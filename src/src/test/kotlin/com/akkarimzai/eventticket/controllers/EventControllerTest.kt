package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.models.category.CreateCategoryCommand
import com.akkarimzai.eventticket.models.event.CreateEventCommand
import com.akkarimzai.eventticket.models.event.UpdateEventCommand
import com.akkarimzai.eventticket.models.ticket.CreateTicketCommand
import com.akkarimzai.eventticket.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventControllerTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val passwordEncoder: PasswordEncoder
): AuthorizedIntegrationTest(userRepository, webTestClient, passwordEncoder) {
    @Test
    fun `create event related to category`() {
        val categoryRequest = CreateCategoryCommand(title = "test category")
        val eventRequest = CreateEventCommand(
            title = "test event",
            artist = "test artist",
            date = LocalDateTime.now().plusDays(7),
            tickets = listOf(
                CreateTicketCommand(
                    title = "test ticket",
                    description = "test description",
                    price = 100.0
                )
            )
        )

        webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .bodyValue(categoryRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .value { categoryId ->
                webTestClient.post()
                    .uri("/api/v1/categories/$categoryId/events")
                    .header("Authorization", "Bearer $adminToken")
                    .bodyValue(eventRequest)
                    .exchange()
                    .expectStatus().isCreated
                    .expectBody(Long::class.java)
                    .value { eventId -> assert(eventId > 0) }
            }
    }

    @Test
    fun `create event forbidden for non admin`() {
        val categoryRequest = CreateCategoryCommand(title = "test category")
        val eventRequest = CreateEventCommand(
            title = "test event",
            artist = "test artist",
            date = LocalDateTime.now().plusDays(7),
            tickets = listOf(
                CreateTicketCommand(
                    title = "test ticket",
                    description = "test description",
                    price = 100.0
                )
            )
        )

        webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .bodyValue(categoryRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .value { categoryId ->
                webTestClient.post()
                    .uri("/api/v1/categories/$categoryId/events")
                    .header("Authorization", "Bearer $userToken")
                    .bodyValue(eventRequest)
                    .exchange()
                    .expectStatus().isForbidden
            }
    }

    @Test
    fun `create event with non existing category`() {
        val eventRequest = CreateEventCommand(
            title = "test event",
            artist = "test artist",
            date = LocalDateTime.now().plusDays(7),
            tickets = listOf(
                CreateTicketCommand(
                    title = "test ticket",
                    description = "test description",
                    price = 100.0
                )
            )
        )
        webTestClient.post()
            .uri("/api/v1/categories/9999/events")
            .header("Authorization", "Bearer $adminToken")
            .bodyValue(eventRequest)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `update event related to category`() {
        val categoryRequest = CreateCategoryCommand(title = "test category")
        val eventRequest = CreateEventCommand(
            title = "test event",
            artist = "test artist",
            date = LocalDateTime.now().plusDays(7),
            tickets = listOf(
                CreateTicketCommand(
                    title = "test ticket",
                    description = "test description",
                    price = 100.0
                )
            )
        )
        val eventUpdateRequest = UpdateEventCommand(
            title = "test event updated", null, null)

        webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .bodyValue(categoryRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .value { categoryId ->
                webTestClient.post()
                    .uri("/api/v1/categories/$categoryId/events")
                    .header("Authorization", "Bearer $adminToken")
                    .bodyValue(eventRequest)
                    .exchange()
                    .expectStatus().isCreated
                    .expectBody(Long::class.java)
                    .value { eventId ->
                        webTestClient.put()
                            .uri("/api/v1/categories/$categoryId/events/$eventId")
                            .header("Authorization", "Bearer $adminToken")
                            .bodyValue(eventUpdateRequest)
                            .exchange()
                            .expectStatus().isNoContent

                        webTestClient.get()
                            .uri("/api/v1/categories/$categoryId/events/$eventId")
                            .exchange()
                            .expectStatus().isOk
                            .expectBody()
                            .jsonPath("$.title").isEqualTo(eventUpdateRequest.title!!)
                            .jsonPath("$.artist").isEqualTo(eventRequest.artist!!)
                    }
            }
    }

    @Test
    fun `update event should not permitted to non admin user`() {
        val categoryRequest = CreateCategoryCommand(title = "test category")
        val eventRequest = CreateEventCommand(
            title = "test event",
            artist = "test artist",
            date = LocalDateTime.now().plusDays(7),
            tickets = listOf(
                CreateTicketCommand(
                    title = "test ticket",
                    description = "test description",
                    price = 100.0
                )
            )
        )
        val eventUpdateRequest = UpdateEventCommand(
            title = "test event updated", null, null)

        webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .bodyValue(categoryRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .value { categoryId ->
                webTestClient.post()
                    .uri("/api/v1/categories/$categoryId/events")
                    .header("Authorization", "Bearer $adminToken")
                    .bodyValue(eventRequest)
                    .exchange()
                    .expectStatus().isCreated
                    .expectBody(Long::class.java)
                    .value { eventId ->
                        webTestClient.put()
                            .uri("/api/v1/categories/$categoryId/events/$eventId")
                            .header("Authorization", "Bearer $userToken")
                            .bodyValue(eventUpdateRequest)
                            .exchange()
                            .expectStatus().isForbidden
                    }
            }
    }

    @Test
    fun `update event with non existing category`() {
        val eventRequest = UpdateEventCommand(title = "test event updated", null, null)
        webTestClient.put()
            .uri("/api/v1/categories/9999/events/9999")
            .header("Authorization", "Bearer $adminToken")
            .bodyValue(eventRequest)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should load all events for any user`() {
        val request = CreateCategoryCommand(title = "test category",
            events = listOf(
                CreateEventCommand(
                    title = "test event",
                    artist = "test artist",
                    date = LocalDateTime.now().plusDays(7),
                    tickets = listOf(
                        CreateTicketCommand(
                            title = "test ticket",
                            description = "test description",
                            price = 100.0
                        )
                    )
                ),
                CreateEventCommand(
                    title = "test event1",
                    artist = "test artist1",
                    date = LocalDateTime.now().plusDays(7),
                    tickets = listOf(
                        CreateTicketCommand(
                            title = "test ticket1",
                            description = "test description1",
                            price = 100.0
                        )
                    )
                ),
                CreateEventCommand(
                    title = "test event2",
                    artist = "test artist2",
                    date = LocalDateTime.now().plusDays(7),
                    tickets = listOf(
                        CreateTicketCommand(
                            title = "test ticket2",
                            description = "test description2",
                            price = 100.0
                        )
                    )
                )
            ))
        webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .value { categoryId ->
                webTestClient.get()
                    .uri("/api/v1/categories/$categoryId/events?page=0&size=10")
                    .exchange()
                    .expectStatus().isOk
                    .expectBody()
                    .jsonPath("$.page.size").isEqualTo(10)
                    .jsonPath("$.page.totalElements").isEqualTo(3)
                    .jsonPath("$.page.totalPages").isEqualTo(1)
            }

    }
}