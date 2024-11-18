package com.akkarimzai.eventticket.models.event

data class EventTicketDto(
    val title: String,
    val description: String?,
    val price: Double,
)
