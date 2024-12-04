package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.models.category.CreateCategoryCommand
import com.akkarimzai.eventticket.models.event.CreateEventCommand
import com.akkarimzai.eventticket.models.order.CreateOrderCommand
import com.akkarimzai.eventticket.models.order.UpdateOrderCommand
import com.akkarimzai.eventticket.models.orderItem.CreateOrderItemCommand
import com.akkarimzai.eventticket.models.orderItem.OrderItemDto
import com.akkarimzai.eventticket.models.orderItem.UpdateOrderItemCommand
import com.akkarimzai.eventticket.models.ticket.CreateTicketCommand
import com.akkarimzai.eventticket.models.ticket.TicketDto
import com.akkarimzai.eventticket.repositories.UserRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val passwordEncoder: PasswordEncoder,
    @Autowired private val objectMapper: ObjectMapper
): AuthorizedIntegrationTest(userRepository, webTestClient, passwordEncoder)  {
    @Test
    fun `should create and load order`() {
        val category = CreateCategoryCommand("test category")
        val event = CreateEventCommand(
            title = "test event",
            artist = "test artist",
            date = LocalDateTime.now().plusDays(7),
            tickets = listOf(
                CreateTicketCommand(
                    title = "test ticket",
                    description = "test description",
                    price = 100.0
                ),
                CreateTicketCommand(
                    title = "test ticket1",
                    description = "test description1",
                    price = 110.0
                ),
                CreateTicketCommand(
                    title = "test ticket2",
                    description = "test description2",
                    price = 110.0
                )
            )
        )

        val categoryId = webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(category)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!

        val eventId = webTestClient.post()
            .uri("/api/v1/categories/$categoryId/events")
            .header("Authorization", "Bearer $adminToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(event)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!

        val ticketDtoList = webTestClient.get()
            .uri("/api/v1/categories/1/events/1/tickets?page=0&size=10")
            .exchange()
            .expectStatus().isOk
            .returnResult(String::class.java)
            .responseBody
            .blockFirst()
            ?.let { json ->
                val embeddedNode: JsonNode = objectMapper.readTree(json)
                    .path("_embedded")
                    .path("ticketDtoList")

                objectMapper.convertValue<List<TicketDto>>(
                    embeddedNode,
                    object : TypeReference<List<TicketDto>>() {}
                )
            }!!

        val orderItemRequests = ticketDtoList.map {
            CreateOrderItemCommand(
                ticketId = it.id,
                amount = ThreadLocalRandom.current().nextInt(1, 20 + 1)
            )
        }
        val orderRequest = CreateOrderCommand(items = orderItemRequests)

        val orderId = webTestClient.post()
            .uri("/api/v1/orders")
            .header("Authorization", "Bearer $adminToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!

        webTestClient.get()
            .uri("/api/v1/orders/$orderId")
            .header("Authorization", "Bearer $adminToken")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(orderId)
    }

    @Test
    fun `permission denied for unauthorized user`() {
        val category = CreateCategoryCommand("test category")
        val event = CreateEventCommand(
            title = "test event",
            artist = "test artist",
            date = LocalDateTime.now().plusDays(7),
            tickets = listOf(
                CreateTicketCommand(
                    title = "test ticket",
                    description = "test description",
                    price = 100.0
                ),
                CreateTicketCommand(
                    title = "test ticket1",
                    description = "test description1",
                    price = 110.0
                ),
                CreateTicketCommand(
                    title = "test ticket2",
                    description = "test description2",
                    price = 110.0
                )
            )
        )

        val categoryId = webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(category)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!

        val eventId = webTestClient.post()
            .uri("/api/v1/categories/$categoryId/events")
            .header("Authorization", "Bearer $adminToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(event)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!

        val ticketDtoList = webTestClient.get()
            .uri("/api/v1/categories/1/events/1/tickets?page=0&size=10")
            .exchange()
            .expectStatus().isOk
            .returnResult(String::class.java)
            .responseBody
            .blockFirst()
            ?.let { json ->
                val embeddedNode: JsonNode = objectMapper.readTree(json)
                    .path("_embedded")
                    .path("ticketDtoList")

                objectMapper.convertValue<List<TicketDto>>(
                    embeddedNode,
                    object : TypeReference<List<TicketDto>>() {}
                )
            }!!

        val orderItemRequests = ticketDtoList.map {
            CreateOrderItemCommand(
                ticketId = it.id,
                amount = ThreadLocalRandom.current().nextInt(1, 20 + 1)
            )
        }
        val orderRequest = CreateOrderCommand(items = orderItemRequests)

         webTestClient.post()
            .uri("/api/v1/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun `should return bad request on invalid command`() {
        val category = CreateCategoryCommand("test category")
        val event = CreateEventCommand(
            title = "test event",
            artist = "test artist",
            date = LocalDateTime.now().plusDays(7),
            tickets = listOf(
                CreateTicketCommand(
                    title = "test ticket",
                    description = "test description",
                    price = 100.0
                ),
                CreateTicketCommand(
                    title = "test ticket1",
                    description = "test description1",
                    price = 110.0
                ),
                CreateTicketCommand(
                    title = "test ticket2",
                    description = "test description2",
                    price = 110.0
                )
            )
        )

        val categoryId = webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(category)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!

        val eventId = webTestClient.post()
            .uri("/api/v1/categories/$categoryId/events")
            .header("Authorization", "Bearer $adminToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(event)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!

        val ticketDtoList = webTestClient.get()
            .uri("/api/v1/categories/1/events/1/tickets?page=0&size=10")
            .exchange()
            .expectStatus().isOk
            .returnResult(String::class.java)
            .responseBody
            .blockFirst()
            ?.let { json ->
                val embeddedNode: JsonNode = objectMapper.readTree(json)
                    .path("_embedded")
                    .path("ticketDtoList")

                objectMapper.convertValue<List<TicketDto>>(
                    embeddedNode,
                    object : TypeReference<List<TicketDto>>() {}
                )
            }!!

        val orderItemRequests = ticketDtoList.map {
            CreateOrderItemCommand(
                ticketId = it.id,
                amount = ThreadLocalRandom.current().nextInt(-20, 0)
            )
        }
        val orderRequest = CreateOrderCommand(items = orderItemRequests)

        webTestClient.post()
            .uri("/api/v1/orders")
            .header("Authorization", "Bearer $userToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should update order items`() {
        val category = CreateCategoryCommand("test category")
        val event = CreateEventCommand(
            title = "test event",
            artist = "test artist",
            date = LocalDateTime.now().plusDays(7),
            tickets = listOf(
                CreateTicketCommand(
                    title = "test ticket",
                    description = "test description",
                    price = 100.0
                ),
                CreateTicketCommand(
                    title = "test ticket1",
                    description = "test description1",
                    price = 110.0
                ),
                CreateTicketCommand(
                    title = "test ticket2",
                    description = "test description2",
                    price = 110.0
                )
            )
        )

        val categoryId = webTestClient.post()
            .uri("/api/v1/categories")
            .header("Authorization", "Bearer $adminToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(category)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!

        val eventId = webTestClient.post()
            .uri("/api/v1/categories/$categoryId/events")
            .header("Authorization", "Bearer $adminToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(event)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!

        val ticketDtoList = webTestClient.get()
            .uri("/api/v1/categories/1/events/1/tickets?page=0&size=10")
            .exchange()
            .expectStatus().isOk
            .returnResult(String::class.java)
            .responseBody
            .blockFirst()
            ?.let { json ->
                val embeddedNode: JsonNode = objectMapper.readTree(json)
                    .path("_embedded")
                    .path("ticketDtoList")

                objectMapper.convertValue<List<TicketDto>>(
                    embeddedNode,
                    object : TypeReference<List<TicketDto>>() {}
                )
            }!!

        val orderItemRequests = ticketDtoList.map {
            CreateOrderItemCommand(
                ticketId = it.id,
                amount = ThreadLocalRandom.current().nextInt(1, 20)
            )
        }
        val orderRequest = CreateOrderCommand(items = orderItemRequests)

        val orderId = webTestClient.post()
            .uri("/api/v1/orders")
            .header("Authorization", "Bearer $userToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody!!

        val orderItems = webTestClient.get()
            .uri("/api/v1/orders/$orderId/items")
            .header("Authorization", "Bearer $userToken")
            .exchange()
            .expectStatus().isOk
            .returnResult(String::class.java)
            .responseBody
            .blockFirst()
            ?.let { json ->
                val embeddedNode: JsonNode = objectMapper.readTree(json)
                    .path("_embedded")
                    .path("orderItemDtoList")

                objectMapper.convertValue<List<OrderItemDto>>(
                    embeddedNode,
                    object : TypeReference<List<OrderItemDto>>() {}
                )
            }!!

        val updatedOrderItemRequests = orderItems.map {
            UpdateOrderItemCommand(
                orderItemId = it.id,
                amount = ThreadLocalRandom.current().nextInt(1, 20))
        }
        val updatedOrderRequest = UpdateOrderCommand(items = updatedOrderItemRequests)

        webTestClient.put()
            .uri("/api/v1/orders/$orderId")
            .header("Authorization", "Bearer $userToken")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updatedOrderRequest)
            .exchange()
            .expectStatus().isNoContent
    }
}