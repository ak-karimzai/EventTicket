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

fun CreateOrderCommand.toOrder(createdBy: User): Order {
    return Order(
        user = createdBy,
    )
}