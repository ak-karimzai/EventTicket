package com.akkarimzai.eventticket.profiles

import com.akkarimzai.eventticket.entities.Event
import com.akkarimzai.eventticket.entities.Ticket
import com.akkarimzai.eventticket.models.ticket.CreateTicketCommand
import com.akkarimzai.eventticket.models.ticket.TicketDto
import com.akkarimzai.eventticket.models.ticket.UpdateTicketCommand

fun Ticket.toDto(): TicketDto {
    return TicketDto(
        id = this.id!!,
        title = this.title,
        description = this.description,
        price = this.price
    )
}

fun CreateTicketCommand.toTicket(event: Event): Ticket {
    return Ticket(
        title = this.title,
        description = this.description,
        price = this.price,
        event = event,
    )
}

fun UpdateTicketCommand.toTicket(ticket: Ticket): Ticket {
    return Ticket(
        id = ticket.id,
        title = this.title ?: ticket.title,
        description = this.description ?: ticket.description,
        price = this.price ?: ticket.price,
        event = ticket.event,
    )
}