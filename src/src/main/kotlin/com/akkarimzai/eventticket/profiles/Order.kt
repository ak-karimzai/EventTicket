package com.akkarimzai.eventticket.profiles

import com.akkarimzai.eventticket.entities.Order
import com.akkarimzai.eventticket.entities.OrderItem
import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.models.order.*
import com.akkarimzai.eventticket.models.orderItem.OrderItemDto
import com.akkarimzai.eventticket.repositories.TicketRepository

fun Order.toDto(): OrderDto {
    return OrderDto(
        id = this.id!!,
        items = this.items.map { it.toDto() },
        orderPlaced = this.orderPlaced,
        orderPaid = this.orderPaid
    )
}

fun OrderItem.toDto(): OrderItemDto {
    return OrderItemDto(
        ticket = this.ticket.toDto(),
        count = this.amount
    )
}

fun CreateOrderCommand.toOrder(createdBy: User, ticketRepository: TicketRepository): Order {
    return Order(
        user = createdBy,
        items = this.items.map { it.toOrderItem(ticketRepository = ticketRepository) }.toMutableList()
    ).also { order ->
        order.items.forEach { item ->
            item.order = order
        }
    }
}




fun UpdateOrderCommand.toOrder(order: Order, ticketRepository: TicketRepository): Order {
    this.items?.forEach { itemDto ->
        val item = itemDto.toOrderItem(ticketRepository = ticketRepository)
        val orderItem = order.items.firstOrNull { orderItem -> orderItem.ticket.id == item.ticket.id }
        if (orderItem != null) {
            orderItem.amount = item.amount
        } else {
            order.items.add(item)
        }
    }
    return order
}
