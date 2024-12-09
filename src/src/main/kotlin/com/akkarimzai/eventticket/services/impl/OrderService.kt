package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.annotations.Validate
import com.akkarimzai.eventticket.entities.Order
import com.akkarimzai.eventticket.entities.OrderItem
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.exceptions.ForbiddenException
import com.akkarimzai.eventticket.models.order.CreateOrderCommand
import com.akkarimzai.eventticket.models.order.ListOrderQuery
import com.akkarimzai.eventticket.models.order.OrderDto
import com.akkarimzai.eventticket.models.order.UpdateOrderCommand
import com.akkarimzai.eventticket.profiles.toOrder
import com.akkarimzai.eventticket.profiles.toDto
import com.akkarimzai.eventticket.profiles.toOrderItem
import com.akkarimzai.eventticket.repositories.OrderItemRepository
import com.akkarimzai.eventticket.repositories.OrderRepository
import com.akkarimzai.eventticket.repositories.TicketRepository
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Validate
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
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

    @Transactional
    fun create(command: CreateOrderCommand): Long {
        logger.info { "request create $command" }

        val user = authService.currentUser()
        val order = command.toOrder(user)
        val savedOrder = orderRepository.save(order).also {
            logger.debug { "order with id $it create" }
        }

        command.items.map {
            val ticket = ticketRepository.findById(it.ticketId).orElseThrow {
                logger.debug { "ticket with id ${it.ticketId} not found" }
                NotFoundException("ticket", it.ticketId)
            }
            OrderItem(order = savedOrder, ticket = ticket, amount = it.amount)
        }.also {
            orderItemRepository.saveAll(it)
            logger.debug { "order items created" }
        }

        return savedOrder.id!!
    }

    @Transactional
    fun update(orderId: Long, command: UpdateOrderCommand) {
        logger.info { "request update for order: $command" }

        val order = loadById(orderId)
        if (order.orderPaid) {
            logger.debug { "order with id: $orderId already paid" }
            throw ForbiddenException("Order already paid")
        }

        val orderItems = orderItemRepository.findAllByOrderId(orderId)
        command.items?.forEach { item ->
            orderItems.find { it.id == item.orderItemId }?.let {
                it.amount = item.amount
            }
        }

        orderItems.forEach { item ->
            if (item.amount <= 0) {
                orderItemRepository.delete(item)
            }
        }
        orderItemRepository.saveAll(orderItems.filter { it.amount > 0 })
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
