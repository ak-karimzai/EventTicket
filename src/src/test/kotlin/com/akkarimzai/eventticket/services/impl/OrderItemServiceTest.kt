package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.*
import com.akkarimzai.eventticket.exceptions.ForbiddenException
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.orderItem.CreateOrderItemCommand
import com.akkarimzai.eventticket.models.orderItem.ListOrderItemQuery
import com.akkarimzai.eventticket.models.orderItem.OrderItemDto
import com.akkarimzai.eventticket.models.orderItem.UpdateOrderItemCommand
import com.akkarimzai.eventticket.profiles.toDto
import com.akkarimzai.eventticket.profiles.toOrderItem
import com.akkarimzai.eventticket.repositories.OrderItemRepository
import com.akkarimzai.eventticket.repositories.OrderRepository
import com.akkarimzai.eventticket.repositories.TicketRepository
import com.akkarimzai.eventticket.services.AuthService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.*

class OrderItemServiceTest : FunSpec({
    val orderItemRepository: OrderItemRepository = mockk()
    val ticketRepository: TicketRepository = mockk()
    val orderRepository: OrderRepository = mockk()
    val authService: AuthService = mockk()
    val orderItemService = OrderItemService(
        orderItemRepository, ticketRepository, orderRepository, authService)

    val user = User(id = 1L, name = "testtest", email = "email@mail.ru",
        phoneNumber = null, "username", "password", role = Role.USER)
    val orderId = 1L
    val category = Category(id = 1L, title = "Test Category")
    val event = Event(id = 1L, title = "Event 1", artist = "Artist 1", date = LocalDateTime.now(), category = category)
    val ticket = Ticket(id = 1L, event = event, title = "Test Ticket", price = 43.2)
    val order = Order(id = orderId, user = user, orderPlaced = LocalDateTime.now(),
        orderPaid = true, items = mutableListOf()
    )
    val orderItems = mutableListOf(
        OrderItem(id = 1L, order = order, ticket = ticket, amount = 1),
        OrderItem(id = 2L, order = order, ticket = ticket, amount = 1),
        OrderItem(id = 3L, order = order, ticket = ticket, amount = 1)
    )
    order.items = orderItems

    beforeEach {
        clearMocks(orderItemRepository, ticketRepository, orderRepository, authService)
    }

    context("list order items") {
        test("order id not exist") {
            // Arrange
            val orderId = 1L
            every { orderRepository.existsById(orderId) } returns false
            val query = ListOrderItemQuery(orderId = orderId, page = 0, size = 10)

            // Act && Assert
            assertThrows<NotFoundException> {
                orderItemService.list(query)
            }
        }

        test("list items return a paginated list of order items") {
            // Arrange
            val orderId = 1L
            every { orderRepository.existsById(orderId) } returns true
            every { orderRepository.findById(orderId) } returns Optional.of(order)
            every { orderItemRepository.findAllByOrderId(orderId, PageRequest.of(0, 10)) } returns PageImpl(orderItems)
            val query = ListOrderItemQuery(orderId = orderId, page = 0, size = 10)

            // Act
            val result = orderItemService.list(query)

            // Assert
            result shouldBe PageImpl(orderItems.map { it.toDto() })
        }
    }

    context("create order item") {
        test("order id not exist") {
            // Arrange
            val orderId = 1L
            every { orderRepository.findById(orderId) } returns Optional.empty()
            val command = mockk<CreateOrderItemCommand>()

            // Act && Assert
            shouldThrow<NotFoundException> {
                orderItemService.create(orderId, command)
            }
        }

        test("order id not related to authorized user") {
            // Arrange
            val orderId = 1L
            val order = Order(
                id = orderId,
                user = User(id = 2L, name = "testtest", email = "email@mail.ru",
                    phoneNumber = null, "username", "password", role = Role.USER),
                orderPlaced = LocalDateTime.now(), orderPaid = true, items = mutableListOf())
            every { orderRepository.existsById(orderId) } returns true
            every { orderRepository.findById(orderId) } returns Optional.of(order)
            every { authService.currentUser() } returns user
            val command = CreateOrderItemCommand(ticketId = 1L, amount = 1)

            // Act && Assert
            shouldThrow<ForbiddenException> {
                orderItemService.create(orderId, command)
            }
        }

        test("create order item") {
            // Arrange
            val orderId = 1L
            every { orderRepository.existsById(orderId) } returns true
            every { orderRepository.findById(orderId) } returns Optional.of(order)
            every { authService.currentUser() } returns user
            val command = CreateOrderItemCommand(ticketId = 1L, amount = 1)
            every { ticketRepository.findById(command.ticketId) } returns Optional.of(ticket)
            every { orderItemRepository.save(any()) } returns orderItems[0]

            // Act
            val result = orderItemService.create(orderId, command)

            // Assert
            result shouldBe 1
        }
    }

    context("test update order item") {
        test("order id not exist") {
            // Arrange
            val orderId = 1L
            val orderItemId = 1L
            val command = UpdateOrderItemCommand(orderItemId = 1L, amount = 1)
            every { orderRepository.findById(orderId) } returns Optional.empty()
            every { orderItemRepository.findById(orderItemId) } returns Optional.empty()

            // Act && Assert
            shouldThrow<NotFoundException> {
                orderItemService.update(orderId, command)
            }
        }

        test("order id not related to authorized user") {
            // Arrange
            val orderId = 1L
            val orderItemId = 1L

            val unrelatedUser = User(
                id = 2L,
                name = "Unrelated User",
                email = "unrelated@mail.ru",
                phoneNumber = null,
                username = "unrelated_username",
                password = "password",
                role = Role.USER
            )
            val order = Order(
                id = orderId,
                user = unrelatedUser,
                orderPlaced = LocalDateTime.now(),
                orderPaid = true,
                items = mutableListOf()
            )

            val orderItem = OrderItem(
                id = orderItemId,
                order = order,
                ticket = ticket,
                amount = 1
            )

            every { orderRepository.existsById(orderId) } returns true
            every { orderRepository.findById(orderId) } returns Optional.of(order)
            every { authService.currentUser() } returns user
            every { orderItemRepository.findById(orderItemId) } returns Optional.of(orderItem)

            val command = UpdateOrderItemCommand(orderItemId = orderItemId, amount = 2)

            // Act && Assert
            shouldThrow<ForbiddenException> {
                orderItemService.update(orderId, command)
            }

            verify(exactly = 0) { orderItemRepository.save(any()) }
        }

        test("update order item") {
            // Arrange
            val orderId = 1L
            val orderItemId = 1L
            every { orderRepository.existsById(orderId) } returns true
            every { orderRepository.findById(orderId) } returns Optional.of(order)
            every { authService.currentUser() } returns user
            every { orderItemRepository.findById(orderItemId) } returns Optional.of(orderItems[0])
            every { orderItemRepository.save(any()) } returns orderItems[0]
            val command = UpdateOrderItemCommand(orderItemId = orderItemId, amount = 2)

            // Act
            orderItemService.update(orderId, command)

            // Assert
            verify(exactly = 1) { orderItemRepository.save(any()) }
        }
    }

    context("test delete order item") {
        test("order id not exist") {
            // Arrange
            val orderId = 1L
            val orderItemId = 1L
            every { orderRepository.findById(orderId) } returns Optional.empty()
            every { orderItemRepository.findById(orderItemId) } returns Optional.empty()

            // Act && Assert
            shouldThrow<NotFoundException> {
                orderItemService.delete(orderId, orderItemId)
            }
        }

        test("order id not related to authorized user") {
            // Arrange
            val orderId = 1L
            val orderItemId = 1L
            val unrelatedUser = User(
                id = 2L,
                name = "Unrelated User",
                email = "unrelated@mail.ru",
                phoneNumber = null,
                username = "unrelated_username",
                password = "password",
                role = Role.USER
            )
            val order = Order(
                id = orderId,
                user = unrelatedUser,
                orderPlaced = LocalDateTime.now(),
                orderPaid = true,
                items = mutableListOf()
            )

            val orderItem = OrderItem(
                id = orderItemId,
                order = order,
                ticket = ticket,
                amount = 1
            )

            every { orderRepository.existsById(orderId) } returns true
            every { orderRepository.findById(orderId) } returns Optional.of(order)
            every { authService.currentUser() } returns user
            every { orderItemRepository.findById(orderItemId) } returns Optional.of(orderItem)

            // Act && Assert
            shouldThrow<ForbiddenException> {
                orderItemService.delete(orderId, orderItemId)
            }

            verify(exactly = 0) { orderItemRepository.delete(any()) }
        }

        test("delete order item") {
            // Arrange
            val orderId = 1L
            val orderItemId = 1L
            every { orderRepository.existsById(orderId) } returns true
            every { orderRepository.findById(orderId) } returns Optional.of(order)
            every { authService.currentUser() } returns user
            every { orderItemRepository.findById(orderItemId) } returns Optional.of(orderItems[0])
            every { orderItemRepository.delete(any()) } just runs

            // Act
            orderItemService.delete(orderId, orderItemId)

            // Assert
            verify(exactly = 1) { orderItemRepository.delete(any()) }
        }
    }

    context("test load order item") {
        test("order id not exist") {
            // Arrange
            val orderId = 1L
            val orderItemId = 1L
            every { orderRepository.findById(orderId) } returns Optional.empty()
            every { orderItemRepository.findById(orderItemId) } returns Optional.empty()

            // Act && Assert
            shouldThrow<NotFoundException> {
                orderItemService.load(orderId, orderItemId)
            }
        }

        test("order id not related to authorized user") {
            // Arrange
            val orderId = 1L
            val orderItemId = 1L
            val unrelatedUser = User(
                id = 2L,
                name = "Unrelated User",
                email = "unrelated@mail.ru",
                phoneNumber = null,
                username = "unrelated_username",
                password = "password",
                role = Role.USER
            )
            val order = Order(
                id = orderId,
                user = unrelatedUser,
                orderPlaced = LocalDateTime.now(),
                orderPaid = true,
                items = mutableListOf()
            )

            val orderItem = OrderItem(
                id = orderItemId,
                order = order,
                ticket = ticket,
                amount = 1
            )

            every { orderRepository.existsById(orderId) } returns true
            every { orderRepository.findById(orderId) } returns Optional.of(order)
            every { authService.currentUser() } returns user
            every { orderItemRepository.findById(orderItemId) } returns Optional.of(orderItem)

            // Act && Assert
            shouldThrow<ForbiddenException> {
                orderItemService.load(orderId, orderItemId)
            }
        }

        test("load order item") {
            // Arrange
            val orderId = 1L
            val orderItemId = 1L
            every { orderRepository.existsById(orderId) } returns true
            every { orderRepository.findById(orderId) } returns Optional.of(order)
            every { authService.currentUser() } returns user
            every { orderItemRepository.findById(orderItemId) } returns Optional.of(orderItems[0])

            // Act
            orderItemService.load(orderId, orderItemId)

            // Assert
            verify(exactly = 1) { orderItemRepository.findById(orderItemId) }
        }
    }
})