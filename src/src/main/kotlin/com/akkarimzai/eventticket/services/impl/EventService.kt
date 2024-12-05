package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.annotations.Validate
import com.akkarimzai.eventticket.entities.Event
import com.akkarimzai.eventticket.exceptions.BadRequestException
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.event.CreateEventCommand
import com.akkarimzai.eventticket.models.event.EventDto
import com.akkarimzai.eventticket.models.event.ListEventQuery
import com.akkarimzai.eventticket.models.event.UpdateEventCommand
import com.akkarimzai.eventticket.profiles.toDto
import com.akkarimzai.eventticket.profiles.toEvent
import com.akkarimzai.eventticket.profiles.toTicket
import com.akkarimzai.eventticket.repositories.CategoryRepository
import com.akkarimzai.eventticket.repositories.EventRepository
import com.akkarimzai.eventticket.repositories.TicketRepository
import com.akkarimzai.eventticket.repositories.specs.EventSpecification
import com.akkarimzai.eventticket.services.AuthService
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Validate
open class EventService(
    private val categoryRepository: CategoryRepository,
    private val eventRepository: EventRepository,
    private val ticketRepository: TicketRepository,
    private val authService: AuthService) {
    private val logger = KotlinLogging.logger {}

    @Transactional
    open fun create(categoryId: Long, command: CreateEventCommand): Long {
        logger.info { "Request create event: $command" }

        if (categoryId <= 0) {
            logger.error { "category id: $categoryId <= 0" }
            throw BadRequestException("invalid category id: $categoryId!")
        }

        val currentUser = authService.currentUser()
        val category = categoryRepository.findById(categoryId).orElseThrow {
            logger.debug { "category with id $categoryId not found" }
            NotFoundException("category", categoryId)
        }

        val event = command.toEvent(currentUser, category)

        val savedEvent = eventRepository.save(event).also {
            logger.debug { "new category with id {$categoryId} created" }
        }

        command.tickets.map { it.toTicket(currentUser, savedEvent) }
            .also {
                ticketRepository.saveAll(it)
                logger.debug { "${it.size} tickets created for event: ${savedEvent.title}" }
            }

        return savedEvent.id!!
    }

    fun update(categoryId: Long, eventId: Long, command: UpdateEventCommand) {
        logger.info { "updating event with id: $eventId, fields to update: $command" }

        val currentUser = authService.currentUser()
        val event = loadById(categoryId, eventId)
        val eventToUpdate = command.toEvent(currentUser, event).also {
            logger.debug { "updated event: $it" }
        }
        eventRepository.save(eventToUpdate).also {
            logger.debug { "event with id: $eventId updated" }
        }
    }

    fun load(categoryId: Long, eventId: Long): EventDto {
        logger.info { "request load event with id: $eventId and category id: $categoryId " }

        return loadById(categoryId, eventId).toDto()
    }

    fun list(query: ListEventQuery): Page<EventDto> {
        logger.info { "request list with query: $query" }

        return eventRepository.findAll(
            EventSpecification.buildSpecification(
                categoryId = query.categoryId,
                title = query.title,
                artist = query.artist,
                from = query.from,
                to = query.to
            ), PageRequest.of(query.page, query.size))
            .map { it.toDto() }.also {
                logger.info { "loaded ${it.size} events" }
            }
    }

    private fun loadById(categoryId: Long, eventId: Long): Event {
        if (categoryId <= 0) {
            logger.error { "category id: $categoryId <= 0" }
            throw BadRequestException("category with id $categoryId not exist!")
        }

        val event = eventRepository.findById(eventId).orElseThrow {
            logger.error { "event with id $eventId not found" }
            throw NotFoundException("event", eventId)
        }

        if (event.category.id != categoryId) {
            logger.debug { "event not related to category with id: $categoryId" }
            throw NotFoundException("event", eventId)
        }

        return event
    }
}
