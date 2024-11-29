package com.akkarimzai.eventticket.profiles

import com.akkarimzai.eventticket.entities.Order
import com.akkarimzai.eventticket.entities.OrderItem
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.orderItem.CreateOrderItemCommand
import com.akkarimzai.eventticket.models.orderItem.UpdateOrderItemCommand
import com.akkarimzai.eventticket.repositories.TicketRepository

fun CreateOrderItemCommand.toOrderItem(order: Order? = null, ticketRepository: TicketRepository): OrderItem {
    val ticket = ticketRepository.findById(this.ticketId).orElseThrow {
        throw NotFoundException("ticket", this.ticketId)
    }
    return OrderItem(
        order = order,
        ticket = ticket,
        amount = this.amount
    )
}

fun UpdateOrderItemCommand.toOrderItem(orderItem: OrderItem
): OrderItem = OrderItem(
        id = orderItem.id,
        order = orderItem.order,
        ticket = orderItem.ticket,
        amount = this.amount)