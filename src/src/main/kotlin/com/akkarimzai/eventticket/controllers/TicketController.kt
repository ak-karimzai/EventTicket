package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.annotations.LogExecutionTime
import com.akkarimzai.eventticket.models.ticket.CreateTicketCommand
import com.akkarimzai.eventticket.models.ticket.ListTicketQuery
import com.akkarimzai.eventticket.models.ticket.TicketDto
import com.akkarimzai.eventticket.models.ticket.UpdateTicketCommand
import com.akkarimzai.eventticket.services.impl.TicketService
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/categories/{categoryId}/events/{eventId}/tickets"])
@LogExecutionTime
class TicketController(private val ticketService: TicketService) {
    @GetMapping
    fun list(
        @PathVariable categoryId: Int,
        @PathVariable eventId: Int,
        @RequestParam title: String? = null,
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        assembler: PagedResourcesAssembler<TicketDto>,
    ): PagedModel<EntityModel<TicketDto>> {
        val ticketList =  ticketService.list(
            query = ListTicketQuery(
                categoryId = categoryId, eventId = eventId,
                title = title, page = page, size = size))
        return assembler.toModel(ticketList)
    }

    @GetMapping("/{ticketId}")
    fun load(
        @PathVariable categoryId: Long,
        @PathVariable eventId: Long,
        @PathVariable ticketId: Long
    ): TicketDto = ticketService.load(
            categoryId = categoryId, eventId = eventId, ticketId = ticketId)

    @PostMapping
    fun create(
        @PathVariable categoryId: Long,
        @PathVariable eventId: Long,
        @RequestBody command: CreateTicketCommand
    ): Long = ticketService.create(categoryId, eventId, command)

    @PutMapping("/{ticketId}")
    fun update(
        @PathVariable categoryId: Long,
        @PathVariable eventId: Long,
        @PathVariable ticketId: Long,
        @RequestBody command: UpdateTicketCommand
    ) = ticketService.update(categoryId, eventId, ticketId, command)
}