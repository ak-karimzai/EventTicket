package com.akkarimzai.eventticket.models.order

import jakarta.validation.Valid

data class CreateOrderCommand(
    @Valid
    val items: List<CommandItemDto>
)