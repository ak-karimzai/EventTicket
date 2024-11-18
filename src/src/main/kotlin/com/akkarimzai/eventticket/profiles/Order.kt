package com.akkarimzai.eventticket.profiles

import com.akkarimzai.eventticket.entities.Order
import com.akkarimzai.eventticket.entities.OrderItem
import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.order.*
import com.akkarimzai.eventticket.repositories.TicketRepository

fun Order.toOrderDto(): OrderDto {
    return OrderDto(
        id = this.id!!,
        items = this.items.map { it.toOrderItemDto() },
        orderPlaced = this.orderPlaced,
        orderPaid = this.orderPaid
    )
}

fun OrderItem.toOrderItemDto(): OrderItemDto {
    return OrderItemDto(
        ticket = this.ticket.toTicketDto(),
        count = this.amount
    )
}

fun CreateOrderCommand.toOrder(createdBy: User, ticketRepository: TicketRepository): Order {
    return Order(
        user = createdBy,
        items = this.items.map { it.toOrderItem(ticketRepository) }.toMutableList()
    ).also { order ->
        order.items.forEach { item ->
            item.order = order
        }
    }
}

private fun CommandItemDto.toOrderItem(ticketRepository: TicketRepository): OrderItem {
    val ticket = ticketRepository.findById(this.ticketId).orElseThrow {
        throw NotFoundException("ticket", this.ticketId)
    }
    return OrderItem(
        order = null,
        ticket = ticket,
        amount = this.count
    )
}


fun UpdateOrderCommand.toOrder(order: Order, ticketRepository: TicketRepository): Order {
    this.items?.forEach { itemDto ->
        val item = itemDto.toOrderItem(ticketRepository)
        val orderItem = order.items.firstOrNull { orderItem -> orderItem.ticket.id == item.ticket.id }
        if (orderItem != null) {
            orderItem.amount = item.amount
        } else {
            order.items.addLast(item)
        }
    }
    return order
}
