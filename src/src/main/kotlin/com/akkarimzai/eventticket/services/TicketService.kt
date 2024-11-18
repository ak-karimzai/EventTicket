package com.akkarimzai.eventticket.services

import com.akkarimzai.eventticket.entities.Ticket
import com.akkarimzai.eventticket.exceptions.BadRequestException
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.ticket.CreateTicketCommand
import com.akkarimzai.eventticket.profiles.toTicket
import com.akkarimzai.eventticket.repositories.EventRepository
import com.akkarimzai.eventticket.repositories.TicketRepository
import mu.KotlinLogging
import kotlin.math.log

class TicketService(
    private val ticketRepository: TicketRepository,
    private val eventRepository: EventRepository,
    private val userService: UserService
) {
    private val logger = KotlinLogging.logger {}

    fun load(ticketId: Long): Ticket {
        logger.info { "loading ticket with id: $ticketId" }

        if (ticketId <= 0) {
            logger.debug { "ticket id: $ticketId <= 0" }
            throw BadRequestException("invalid ticket id!")
        }

        return ticketRepository.findById(ticketId).orElseThrow {
            logger.info { "loading ticket with id: $ticketId not found" }

            NotFoundException("ticket", ticketId)
        }
    }

    fun create(eventId: Long, command: CreateTicketCommand): Long {
        logger.info { "Request create: $command" }

        val user = userService.currentUser()

        val event = eventRepository.findById(eventId).orElseThrow {
            NotFoundException("event", eventId)
        }
        val ticket = command.toTicket(event, user)

        return ticketRepository.save(ticket).id!!.also {
            logger.info { "new ticket with id: $it created" }
        }
    }
}
