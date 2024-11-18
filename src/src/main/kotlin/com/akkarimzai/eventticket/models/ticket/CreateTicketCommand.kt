package com.akkarimzai.eventticket.models.ticket

data class CreateTicketCommand(
    val title: String,
    val description: String?,
    val price: Double,
)