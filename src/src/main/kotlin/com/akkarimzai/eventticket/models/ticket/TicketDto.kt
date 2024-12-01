package com.akkarimzai.eventticket.models.ticket

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Ticket data transfer object")
data class TicketDto(
    @Schema(description = "Ticket ID", example = "1", required = true)
    val id: Long,

    @Schema(description = "Ticket title", example = "Example Event", required = true)
    val title: String,

    @Schema(description = "Ticket description", example = "This is an example event", required = false)
    val description: String?,

    @Schema(description = "Ticket price", example = "10.99", required = true)
    val price: Double
)