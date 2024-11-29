package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.OrderItem
import com.akkarimzai.eventticket.exceptions.BadRequestException
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.exceptions.ForbiddenException
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
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class OrderItemService(
    private val orderItemRepository: OrderItemRepository,
    private val ticketRepository: TicketRepository,
    private val orderRepository: OrderRepository,
    private val authService: AuthService
) {
    val logger = KotlinLogging.logger {}

    fun list(query: ListOrderItemQuery): Page<OrderItemDto> {
        logger.info { "request list order items: $query" }

        orderRepository.existsById(query.orderId).let { exists ->
            if (!exists) {
                logger.debug { "order with id: ${query.orderId} not found" }
                throw NotFoundException("order", query.orderId)
            }
        }
        
        val items = orderItemRepository.findAllByOrderId(query.orderId,
            PageRequest.of(query.page, query.size))
        return items.map { it.toDto() }.also {
            logger.info { "fetched ${it.size} order items" }
        }
    }

    fun create(orderId: Long, command: CreateOrderItemCommand): Long {
        logger.info { "request create order item: $command" }

        if (orderId <= 0) {
            logger.debug { "order id: $orderId <= 0" }
            throw BadRequestException("invalid order id: $orderId!")
        }

        val order = orderRepository.findById(orderId).orElseThrow {
            logger.debug { "order with id: $orderId not found" }
            NotFoundException("order", orderId)
        }

        if (order.user.id != authService.currentUser().id) {
            logger.debug { "Order: $orderId not related to authorized user" }
            throw ForbiddenException("Permission denied")
        }

        val orderItem = command.toOrderItem(order, ticketRepository)
        val createdOrderItem = orderItemRepository.save(orderItem)

        return createdOrderItem.id!!.also {
            logger.info { "created order item with id: $it" }
        }
    }

    fun update(orderId: Long, command: UpdateOrderItemCommand) {
        logger.info { "request update order item: $command" }

        if (orderId <= 0) {
            logger.debug { "order id: $orderId <= 0" }
            throw BadRequestException("invalid order id: $orderId!")
        }

        val orderItem = loadOrderItem(orderId, command.orderItemId)
        val updatedOrderItem = command.toOrderItem(orderItem)
        orderItemRepository.save(updatedOrderItem).also {
            logger.info { "updated order item with id: ${command.orderItemId}" }
        }
    }

    fun delete(orderId: Long, orderItemId: Long) {
        logger.info { "request delete order item with id: $orderItemId" }

        val orderItem = loadOrderItem(orderId, orderItemId)

        orderItemRepository.delete(orderItem).also {
            logger.info { "deleted order item with id: $orderItemId" }
        }
    }

    fun load(orderId: Long, orderItemId: Long): OrderItemDto {
        logger.info { "request load order item with id: $orderItemId" }

        val orderItem = loadOrderItem(orderId, orderItemId)

        return orderItem.toDto().also {
            logger.info { "loaded order item with id: $orderItemId" }
        }
    }

    private fun loadOrderItem(orderId: Long, orderItemId: Long): OrderItem {
        val orderItem = orderItemRepository.findById(orderItemId).orElseThrow {
            logger.debug { "order item with id: $orderItemId not found" }
            NotFoundException("order item", orderItemId)
        }
        if (orderItem.order?.id != orderId) {
            logger.debug { "order item with id: $orderItemId not related to order with id: $orderId" }
            throw NotFoundException("order item", orderItemId)
        }

        if (orderItem.order?.user?.id != authService.currentUser().id) {
            logger.debug { "Order: $orderId not related to authorized user" }
            throw ForbiddenException("Permission denied")
        }
        return orderItem
    }
}