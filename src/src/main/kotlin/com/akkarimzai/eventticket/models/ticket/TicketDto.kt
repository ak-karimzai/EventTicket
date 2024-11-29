package com.akkarimzai.eventticket.models.ticket

data class TicketDto(
    val id: Long,
    val title: String,
    val description: String?,
    val price: Double,
)
