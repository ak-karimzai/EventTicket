package com.akkarimzai.eventticket.profiles

import com.akkarimzai.eventticket.entities.Event
import com.akkarimzai.eventticket.entities.Ticket
import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.models.ticket.CreateTicketCommand
import com.akkarimzai.eventticket.models.ticket.TicketDto
import com.akkarimzai.eventticket.models.ticket.UpdateTicketCommand
import java.time.LocalDateTime

fun Ticket.toDto(): TicketDto {
    return TicketDto(
        id = this.id!!,
        title = this.title,
        description = this.description,
        price = this.price
    )
}

fun CreateTicketCommand.toTicket(user: User, event: Event): Ticket {
    return Ticket(
        title = this.title,
        description = this.description,
        price = this.price,
        event = event,
    ).also {
        it.createdBy = user
        it.createdDate = LocalDateTime.now()
    }
}

fun UpdateTicketCommand.toTicket(user: User, ticket: Ticket): Ticket {
    this.title?.let { ticket.title = it }
    this.description?.let { ticket.description = it }
    this.price?.let { ticket.price = it }

    return ticket.also {
        it.lastModifiedDate = LocalDateTime.now()
        it.lastModifiedBy = user
    }
}