package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.Order
import com.akkarimzai.eventticket.entities.Role
import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.order.CreateOrderCommand
import com.akkarimzai.eventticket.models.order.ListOrderQuery
import com.akkarimzai.eventticket.models.order.UpdateOrderCommand
import com.akkarimzai.eventticket.profiles.toDto
import com.akkarimzai.eventticket.profiles.toOrder
import com.akkarimzai.eventticket.repositories.OrderRepository
import com.akkarimzai.eventticket.repositories.TicketRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.*

class OrderServiceTest : FunSpec({
    val orderRepository = mockk<OrderRepository>()
    val ticketRepository = mockk<TicketRepository>()
    val authService = mockk<AuthServiceImpl>()

    val orderService = OrderService(orderRepository, ticketRepository, authService)

    val orderId = 1L

    val user = User(id = 1L, name = "testtest", email = "email@mail.ru",
        phoneNumber = null, "username", "password", role = Role.USER)
    every { authService.currentUser() } returns user

    val order = Order(id = 1L, user = user, orderPlaced = LocalDateTime.now(),
        orderPaid = true, items = mutableListOf()
    )

    beforeEach {
        clearMocks(orderRepository, ticketRepository, authService)
    }

    test("load should return order when order exists") {
        // Arrange
        every { orderRepository.findById(orderId) } returns Optional.of(order)

        // Act
        val result = orderService.load(orderId)

        // Assert
        result shouldBe order
    }

    test("load should throw NotFoundException when order does not exist") {
        // Arrange
        every { orderRepository.findById(orderId) } returns Optional.empty()

        // Act && Assert
        shouldThrow<NotFoundException> {
            orderService.load(orderId)
        }
    }

    test("list should return a page of orders for a user") {
        // Arrange
        val orders = listOf(order)
        val page = PageImpl(orders)
        every { orderRepository.findAll(PageRequest.of(0, 10)) } returns page

        // Act
        val result = orderService.list(ListOrderQuery(0, 10))

        // Assert
        result.content shouldBe orders.map { it.toDto() }
    }

    test("create should create a new order and return its ID") {
        // Arrange
        val command = CreateOrderCommand(listOf())
        every { authService.currentUser() } returns user
        every { orderRepository.save(any()) } returns order

        // Act
        val result = orderService.create(command)

        // Assert
        result shouldBe 1L
    }

    test("update should update an existing order") {
        // Arrange
        val command = UpdateOrderCommand(listOf())
        val updatedOrder = command.toOrder(order, ticketRepository)
        every { orderRepository.findById(orderId) } returns Optional.of(order)
        every { orderRepository.save(updatedOrder) } returns updatedOrder

        // Act
        orderService.update(orderId, command)

        // Assert
        verify { orderRepository.save(updatedOrder) }
    }
})
