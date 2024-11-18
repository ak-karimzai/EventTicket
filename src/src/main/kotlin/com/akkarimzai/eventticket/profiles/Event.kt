package com.akkarimzai.eventticket.profiles

import com.akkarimzai.eventticket.entities.Category
import com.akkarimzai.eventticket.entities.Event
import com.akkarimzai.eventticket.entities.Ticket
import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.models.event.CreateEventCommand
import com.akkarimzai.eventticket.models.event.EventTicketDto
import com.akkarimzai.eventticket.models.event.UpdateEventCommand

fun CreateEventCommand.toEvent(category: Category, createdBy: User): Event {
    return Event(
        title = this.title,
        artist = this.artist,
        date = this.date,
        tickets = this.tickets.map { it.toTicket(createdBy) }.toMutableList(),
        category = category,
        createdBy = createdBy
    ).also { event ->
        event.tickets.forEach { ticket ->
            ticket.event = event
        }
    }
}

private fun EventTicketDto.toTicket(createdBy: User): Ticket {
    return Ticket(
        title = this.title,
        description = this.description,
        price = this.price,
        event = null,
        createdBy = createdBy
    )
}

fun UpdateEventCommand.toEvent(event: Event): Event {
    return Event(
        id = event.id,
        title = this.title ?: event.title,
        artist = this.artist ?: event.artist,
        date = this.date ?: event.date,
        tickets = event.tickets,
        category = event.category,
        createdBy = event.createdBy
    )
}