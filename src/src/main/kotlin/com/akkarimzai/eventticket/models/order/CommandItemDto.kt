package com.akkarimzai.eventticket.models.order

import jakarta.validation.constraints.Min

data class CommandItemDto(
    @Min(1)
    val ticketId: Long,
    @Min(0)
    val count: Int
)