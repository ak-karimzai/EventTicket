package com.akkarimzai.eventticket.profiles

import com.akkarimzai.eventticket.entities.Category
import com.akkarimzai.eventticket.entities.Event
import com.akkarimzai.eventticket.entities.Ticket
import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.models.event.CreateEventCommand
import com.akkarimzai.eventticket.models.event.EventDto
import com.akkarimzai.eventticket.models.event.EventTicketDto
import com.akkarimzai.eventticket.models.event.UpdateEventCommand
import java.time.LocalDateTime

fun CreateEventCommand.toEvent(user: User, category: Category): Event {
    return Event(
        title = this.title,
        artist = this.artist,
        date = this.date,
        category = category,
    ).also {
        it.createdBy = user
        it.createdDate = LocalDateTime.now()
    }
}

private fun EventTicketDto.toTicket(): Ticket {
    return Ticket(
        title = this.title,
        description = this.description,
        price = this.price,
        event = null,
    )
}

fun UpdateEventCommand.toEvent(user: User, event: Event): Event {
    this.title?.let { event.title = it }
    this.artist?.let { event.artist = it }
    this.date?.let { event.date = it }
    return event.also {
        it.lastModifiedBy = user
        it.lastModifiedDate = LocalDateTime.now()
    }
}

fun Event.toDto(): EventDto {
    return EventDto(
        id = this.id!!,
        title = this.title,
        artist = this.artist,
        date = this.date,
    )
}
