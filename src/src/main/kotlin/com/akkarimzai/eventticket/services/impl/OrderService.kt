package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.annotations.Validate
import com.akkarimzai.eventticket.entities.Order
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.exceptions.ForbiddenException
import com.akkarimzai.eventticket.models.order.CreateOrderCommand
import com.akkarimzai.eventticket.models.order.ListOrderQuery
import com.akkarimzai.eventticket.models.order.OrderDto
import com.akkarimzai.eventticket.models.order.UpdateOrderCommand
import com.akkarimzai.eventticket.profiles.toOrder
import com.akkarimzai.eventticket.profiles.toDto
import com.akkarimzai.eventticket.repositories.OrderRepository
import com.akkarimzai.eventticket.repositories.TicketRepository
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
@Validate
class OrderService(
    private val orderRepository: OrderRepository,
    private val ticketRepository: TicketRepository,
    private val authService: AuthServiceImpl
) {
    private val logger = KotlinLogging.logger {}

    fun load(orderId: Long): OrderDto {
        logger.info { "loading order with id: $orderId" }

        return loadById(orderId).toDto()
    }

    fun list(query: ListOrderQuery): Page<OrderDto> {
        logger.info { "request list with query: $query" }

        val currentUser = authService.currentUser()
        val userId = currentUser.id

        logger.info { "requested user id: $userId" }

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

        val order = loadById(orderId)
        val updatedOrder = command.toOrder(order, ticketRepository)

        orderRepository.save(updatedOrder)
    }

    private fun loadById(orderId: Long): Order {
        val order = orderRepository.findById(orderId).orElseThrow {
            logger.debug { "order with id: $orderId not found" }

            throw NotFoundException("order", orderId)
        }

        if (order.user.id != authService.currentUser().id) {
            logger.debug { "Order: $orderId not related to authorized user" }
            throw ForbiddenException("Permission denied")
        }

        return order
    }
}
