package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.Order
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.order.CreateOrderCommand
import com.akkarimzai.eventticket.models.order.ListOrderQuery
import com.akkarimzai.eventticket.models.order.OrderDto
import com.akkarimzai.eventticket.models.order.UpdateOrderCommand
import com.akkarimzai.eventticket.profiles.toOrder
import com.akkarimzai.eventticket.profiles.toDto
import com.akkarimzai.eventticket.repositories.OrderRepository
import com.akkarimzai.eventticket.repositories.TicketRepository
import com.akkarimzai.eventticket.services.UserService
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val ticketRepository: TicketRepository,
    private val authService: AuthService
) {
    private val logger = KotlinLogging.logger {}

    fun load(orderId: Long): Order {
        logger.info { "loading order with id: $orderId" }

        return orderRepository.findById(orderId).orElseThrow {
            logger.debug { "order with id: $orderId not found" }

            throw NotFoundException("order", orderId)
        }
    }

    fun list(userId: Long, query: ListOrderQuery): Page<OrderDto> {
        logger.info { "request list for user: $userId" }

        return orderRepository
            .findAll(PageRequest.of(query.page, query.size))
            .map { it.toDto() }
    }

    fun create(command: CreateOrderCommand): Long {
        logger.info { "request create $command" }

        val user = authService.currentUser()
        val order = command.toOrder(user, ticketRepository)

        return orderRepository.save(order).id!!.also {
            logger.debug { "order with id $it create" }
        }
    }

    fun update(orderId: Long, command: UpdateOrderCommand) {
        logger.info { "request update for order: $command" }

        val order = load(orderId)
        val updatedOrder = command.toOrder(order, ticketRepository)

        orderRepository.save(order)
    }
}
