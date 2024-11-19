package com.akkarimzai.eventticket.models.event

import jakarta.validation.constraints.DecimalMin
import org.hibernate.validator.constraints.Length

data class EventTicketDto(
    @Length(min = 3, max = 256)
    val title: String,
    @Length(min = 3, max = 256)
    val description: String?,
    @DecimalMin(value = "0.0", inclusive = true)
    val price: Double,
)
