package com.akkarimzai.eventticket.services

import com.akkarimzai.eventticket.entities.Ticket
import com.akkarimzai.eventticket.exceptions.BadRequestException
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.ticket.CreateTicketCommand
import com.akkarimzai.eventticket.models.ticket.ListTicketQuery
import com.akkarimzai.eventticket.models.ticket.TicketDto
import com.akkarimzai.eventticket.models.ticket.UpdateTicketCommand
import com.akkarimzai.eventticket.profiles.toTicket
import com.akkarimzai.eventticket.profiles.toDto
import com.akkarimzai.eventticket.repositories.EventRepository
import com.akkarimzai.eventticket.repositories.TicketRepository
import com.akkarimzai.eventticket.repositories.specs.TicketSpecification
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class TicketService(
    private val ticketRepository: TicketRepository,
    private val eventRepository: EventRepository,
) {
    private val logger = KotlinLogging.logger {}

    fun load(eventId: Long, ticketId: Long): TicketDto {
        logger.info { "loading ticket with id: $ticketId" }

        return loadById(eventId, ticketId).toDto()
    }

    fun create(eventId: Long, command: CreateTicketCommand): Long {
        logger.info { "Request create: $command" }

        val event = eventRepository.findById(eventId).orElseThrow {
            NotFoundException("event", eventId)
        }
        val ticket = command.toTicket(event)

        return ticketRepository.save(ticket).id!!.also {
            logger.info { "new ticket with id: $it created" }
        }
    }

    fun update(eventId: Long, ticketId: Long, command: UpdateTicketCommand) {
        logger.info { "request update: $command" }

        val ticket = loadById(eventId, ticketId)
        val ticketToUpdate = command.toTicket(ticket)
        ticketRepository.save(ticketToUpdate).also {
            logger.debug { "ticket with id $ticketId updated" }
        }
    }

    fun list(eventId: Long?, query: ListTicketQuery): Page<TicketDto> {
        logger.info { "request list with query: $query" }

        eventId?.let {
            if (it <= 0) {
                logger.error { "invalid id: $eventId <= 0" }
                throw BadRequestException("invalid event id!")
            }
        }

        return ticketRepository.findAll(TicketSpecification.buildSpecification(
            eventId = eventId,
            title = query.title),
            pageable = PageRequest.of(query.page, query.size)
        ).map { it.toDto() }.also {
            logger.debug { "loaded ${it.size} tickets" }
        }
    }

    private fun loadById(eventId: Long, ticketId: Long): Ticket {
        if (eventId <= 0) {
            logger.debug { "event id: $eventId <= 0" }
            throw BadRequestException("invalid event id!")
        }

        if (ticketId <= 0) {
            logger.debug { "ticket id: $ticketId <= 0" }
            throw BadRequestException("invalid ticket id!")
        }

        val ticket = ticketRepository.findById(ticketId).orElseThrow {
            logger.info { "loading ticket with id: $ticketId not found" }

            NotFoundException("ticket", ticketId)
        }

        if (ticket.event?.id != eventId) {
            logger.error { "blocking user to access ticket: $ticketId " +
                    "with invalid $ticketId" }
            throw NotFoundException("ticket", ticketId)
        }
        return ticket
    }
}
