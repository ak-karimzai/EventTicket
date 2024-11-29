package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.annotations.LogExecutionTime
import com.akkarimzai.eventticket.models.event.CreateEventCommand
import com.akkarimzai.eventticket.models.event.EventDto
import com.akkarimzai.eventticket.models.event.ListEventQuery
import com.akkarimzai.eventticket.models.event.UpdateEventCommand
import com.akkarimzai.eventticket.services.impl.EventService
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping(value = ["/api/v1/categories/{categoryId}/events"])
@LogExecutionTime
class EventController(private val eventService: EventService) {
    @GetMapping
    fun list(
        @PathVariable categoryId: Long,
        @RequestParam title: String? = null,
        @RequestParam artist: String? = null,
        @RequestParam from: LocalDateTime? = null,
        @RequestParam to: LocalDateTime? = null,
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        assembler: PagedResourcesAssembler<EventDto>
    ): PagedModel<EntityModel<EventDto>> {
        val categoryList = eventService.list(
            query = ListEventQuery(
                categoryId = categoryId, title = title, artist = artist,
                from = from, to = to, page = page, size = size)
        )
        return assembler.toModel(categoryList)
    }

    @GetMapping("/{eventId}")
    fun load(
        @PathVariable categoryId: Long,
        @PathVariable eventId: Long,
    ): EventDto = eventService.load(
            categoryId = categoryId, eventId = eventId)

    @PostMapping
    fun create(
        @PathVariable categoryId: Long,
        @RequestBody command: CreateEventCommand
    ): Long = eventService.create(
            categoryId = categoryId, command = command)

    @PutMapping("/{eventId}")
    fun update(
        @PathVariable categoryId: Long,
        @PathVariable eventId: Long,
        @RequestBody command: UpdateEventCommand
    ) = eventService.update(
            categoryId = categoryId, eventId = eventId,
            command = command)
}