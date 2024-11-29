package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.annotations.Validate
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
@Validate
class TicketService(
    private val ticketRepository: TicketRepository,
    private val eventRepository: EventRepository,
) {
    private val logger = KotlinLogging.logger {}

    fun load(categoryId: Long, eventId: Long, ticketId: Long): TicketDto {
        logger.info { "loading ticket with id: $ticketId" }

        return loadById(categoryId, eventId, ticketId).toDto()
    }

    fun create(categoryId: Long, eventId: Long, command: CreateTicketCommand): Long {
        logger.info { "Request create: $command" }

        val event = eventRepository.findById(eventId).orElseThrow {
            NotFoundException("event", eventId)
        }
        val ticket = command.toTicket(event)

        return ticketRepository.save(ticket).id!!.also {
            logger.info { "new ticket with id: $it created" }
        }
    }

    fun update(categoryId: Long, eventId: Long, ticketId: Long, command: UpdateTicketCommand) {
        logger.info { "request update: $command" }

        val ticket = loadById(categoryId, eventId, ticketId)
        val ticketToUpdate = command.toTicket(ticket)
        ticketRepository.save(ticketToUpdate).also {
            logger.debug { "ticket with id $ticketId updated" }
        }
    }

    fun list(query: ListTicketQuery): Page<TicketDto> {
        logger.info { "request list with query: $query" }

        return ticketRepository.findAll(TicketSpecification.buildSpecification(
            categoryId = query.categoryId,
            eventId = query.eventId,
            title = query.title),
            pageable = PageRequest.of(query.page, query.size)
        ).map { it.toDto() }.also {
            logger.debug { "loaded ${it.size} tickets" }
        }
    }

    private fun loadById(categoryId: Long, eventId: Long, ticketId: Long): Ticket {
        if (categoryId <= 0) {
            logger.debug { "category id: $categoryId <= 0" }
            throw BadRequestException("invalid category id!")
        }

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

        if (ticket.event?.id != eventId || ticket.event?.category?.id != categoryId) {
            logger.error { "blocking user to access ticket: $ticketId " +
                    "with invalid $ticketId" }
            throw NotFoundException("ticket", ticketId)
        }
        return ticket
    }
}
