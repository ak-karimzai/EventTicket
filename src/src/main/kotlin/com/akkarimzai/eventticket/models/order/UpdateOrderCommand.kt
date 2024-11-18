package com.akkarimzai.eventticket.models.order

import com.akkarimzai.eventticket.annotations.AtLeastOneNotNull
import jakarta.validation.Valid

@AtLeastOneNotNull
data class UpdateOrderCommand(
    @Valid
    val items: List<CommandItemDto>?
)