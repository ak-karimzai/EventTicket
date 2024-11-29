package com.akkarimzai.eventticket.models.orderItem

import com.akkarimzai.eventticket.models.ticket.TicketDto

data class OrderItemDto(
    val ticket: TicketDto,
    val count: Int
)
