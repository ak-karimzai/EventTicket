package com.akkarimzai.eventticket.models.orderItem

import com.akkarimzai.eventticket.models.ticket.TicketDto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Order item data transfer object")
data class OrderItemDto(
    @Schema(description = "Order item ID", example = "1", required = true)
    val id: Long,

    @Schema(description = "Ticket information", required = true)
    val ticket: TicketDto,

    @Schema(description = "Number of tickets", example = "2", required = true)
    val count: Int
)