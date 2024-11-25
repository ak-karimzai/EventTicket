package com.akkarimzai.eventticket.models.order

import com.akkarimzai.eventticket.models.ticket.TicketDto

data class ItemDto(
    val ticket: TicketDto,
    val count: Long
)