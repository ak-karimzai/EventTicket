package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.Event
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.event.CreateEventCommand
import com.akkarimzai.eventticket.models.event.EventDto
import com.akkarimzai.eventticket.models.event.ListEventQuery
import com.akkarimzai.eventticket.models.event.UpdateEventCommand
import com.akkarimzai.eventticket.profiles.toDto
import com.akkarimzai.eventticket.profiles.toEvent
import com.akkarimzai.eventticket.repositories.CategoryRepository
import com.akkarimzai.eventticket.repositories.EventRepository
import com.akkarimzai.eventticket.repositories.specs.EventSpecification
import mu.KotlinLogging
import org.apache.coyote.BadRequestException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class EventService(
    private val categoryRepository: CategoryRepository,
    private val eventRepository: EventRepository) {
    private val logger = KotlinLogging.logger {}

    fun create(categoryId: Long, command: CreateEventCommand): Long {
        logger.info { "Request create event: $command" }

        if (categoryId <= 0) {
            logger.error { "category id: $categoryId <= 0" }
            throw BadRequestException("invalid category id: $categoryId!")
        }

        val category = categoryRepository.findById(categoryId).orElseThrow {
            logger.debug { "category with id $categoryId not found" }
            NotFoundException("category", categoryId)
        }

        val event = command.toEvent(category)

        return eventRepository.save(event).id!!.also {
            logger.debug { "new category with id {$categoryId} created" }
        }
    }

    fun update(categoryId: Long, eventId: Long, command: UpdateEventCommand) {
        logger.info { "updating event with id: $eventId, fields to update: $command" }

        val event = loadById(categoryId, eventId)
        val eventToUpdate = command.toEvent(event).also {
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
