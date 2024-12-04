package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.models.category.CreateCategoryCommand
import com.akkarimzai.eventticket.models.event.CreateEventCommand
import com.akkarimzai.eventticket.models.ticket.CreateTicketCommand
import com.akkarimzai.eventticket.models.ticket.UpdateTicketCommand
import com.akkarimzai.eventticket.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicketControllerTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val passwordEncoder: PasswordEncoder
): AuthorizedIntegrationTest(userRepository, webTestClient, passwordEncoder) {
    @Test
    fun `should create ticket for an existing event`() {
        val category = CreateCategoryCommand(title = "test category")
        val event = CreateEventCommand(title = "test event",
            artist = "test artist",
            date = LocalDateTime.now().plusDays(7),
            tickets = listOf(
                CreateTicketCommand(
                    title = "test ticket",
                    description = "test description",
                    price = 100.0)))
        val ticket = CreateTicketCommand(title = "test ticket",
            description = "test description",
            price = 100.0)

        webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(category)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .value { categoryId ->
                webTestClient.post()
                    .uri("/api/v1/categories/$categoryId/events")
                    .header("Authorization", "Bearer $adminToken")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(event)
                    .exchange()
                    .expectStatus().isCreated
                    .expectBody(Long::class.java)
                    .value { eventId ->
                        webTestClient.post()
                            .uri("/api/v1/categories/$categoryId/events/$eventId/tickets")
                            .header("Authorization", "Bearer $adminToken")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(ticket)
                            .exchange()
                            .expectStatus().isCreated
                    }
            }
    }

    @Test
    fun `load ticket by id`() {
        val category = CreateCategoryCommand(title = "test category")
        val event = CreateEventCommand(title = "test event",
            artist = "test artist",
            date = LocalDateTime.now().plusDays(7),
            tickets = listOf(
                CreateTicketCommand(
                    title = "test ticket",
                    description = "test description",
                    price = 100.0)))
        val ticket = CreateTicketCommand(title = "test ticket",
            description = "test description",
            price = 100.0)

        webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(category)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .value { categoryId ->
                webTestClient.post()
                    .uri("/api/v1/categories/$categoryId/events")
                    .header("Authorization", "Bearer $adminToken")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(event)
                    .exchange()
                    .expectStatus().isCreated
                    .expectBody(Long::class.java)
                    .value { eventId ->
                        webTestClient.post()
                            .uri("/api/v1/categories/$categoryId/events/$eventId/tickets")
                            .header("Authorization", "Bearer $adminToken")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(ticket)
                            .exchange()
                            .expectStatus().isCreated
                            .expectBody(Long::class.java)
                            .value { ticketId ->
                                webTestClient.get()
                                    .uri("/api/v1/categories/$categoryId/events/$eventId/tickets/$ticketId")
                                    .header("Authorization", "Bearer $adminToken")
                                    .exchange()
                                    .expectStatus().isOk
                                    .expectBody()
                                    .jsonPath("\$.title").isEqualTo("test ticket")
                                    .jsonPath("\$.description").isEqualTo("test description")
                                    .jsonPath("\$.price").isEqualTo(100.0)
                            }
                    }
            }
    }

    @Test
    fun `should update an existing ticket`() {
        val category = CreateCategoryCommand(title = "test category")
        val event = CreateEventCommand(title = "test event",
            artist = "test artist",
            date = LocalDateTime.now().plusDays(7),
            tickets = listOf(
                CreateTicketCommand(
                    title = "test ticket",
                    description = "test description",
                    price = 100.0)))
        val ticket = CreateTicketCommand(title = "test ticket",
            description = "test description",
            price = 100.0)
        val updateTicket = UpdateTicketCommand(title = "test ticket updated", null, null)

        webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(category)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .value { categoryId ->
                webTestClient.post()
                    .uri("/api/v1/categories/$categoryId/events")
                    .header("Authorization", "Bearer $adminToken")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(event)
                    .exchange()
                    .expectStatus().isCreated
                    .expectBody(Long::class.java)
                    .value { eventId ->
                        webTestClient.post()
                            .uri("/api/v1/categories/$categoryId/events/$eventId/tickets")
                            .header("Authorization", "Bearer $adminToken")
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(ticket)
                            .exchange()
                            .expectStatus().isCreated
                            .expectBody(Long::class.java)
                            .value { ticketId ->
                                webTestClient.put()
                                    .uri("/api/v1/categories/$categoryId/events/$eventId/tickets/$ticketId")
                                    .header("Authorization", "Bearer $adminToken")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(updateTicket)
                                    .exchange()
                                    .expectStatus().isNoContent

                                webTestClient.get()
                                    .uri("/api/v1/categories/$categoryId/events/$eventId/tickets/$ticketId")
                                    .header("Authorization", "Bearer $adminToken")
                                    .exchange()
                                    .expectStatus().isOk
                                    .expectBody()
                                    .jsonPath("\$.title").isEqualTo("test ticket updated")
                            }
                    }
            }
    }
}