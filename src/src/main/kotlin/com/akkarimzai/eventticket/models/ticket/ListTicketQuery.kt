package com.akkarimzai.eventticket.models.ticket

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class ListTicketQuery(
    @Size(min = 3, max = 60)
    val title: String?,

    @Min(0)
    val page: Int,

    @Min(1)
    val size: Int
)