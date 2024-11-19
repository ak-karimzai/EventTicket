package com.akkarimzai.eventticket.models.ticket

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Size

data class CreateTicketCommand(
    @Size(min = 3, max = 256)
    val title: String,

    @Size(min = 3, max = 256)
    val description: String?,

    @DecimalMin(value = "0.0", inclusive = true)
    val price: Double,
)