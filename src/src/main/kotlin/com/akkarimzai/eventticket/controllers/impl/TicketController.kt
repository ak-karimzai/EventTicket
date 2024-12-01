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

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/categories/{categoryId}/events/{eventId}/tickets"])
@Tag(name = "Ticket API", description = "API for managing tickets")
@LogExecutionTime
class TicketController(private val ticketService: TicketService) {
    @GetMapping
    @Operation(
        summary = "Get a list of tickets",
        description = "Returns a list of tickets",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "List of tickets",
                content = [Content(schema = Schema(implementation = PagedModel::class))]
            )
        ]
    )
    fun list(
        @PathVariable
        @Parameter(
            name = "categoryId",
            description = "Category ID",
            required = true
        )
        categoryId: Int,
        @PathVariable
        @Parameter(
            name = "eventId",
            description = "Event ID",
            required = true
        )
        eventId: Int,
        @RequestParam
        @Parameter(
            name = "title",
            description = "Ticket title",
            required = false
        )
        title: String? = null,
        @RequestParam
        @Parameter(
            name = "page",
            description = "Page number",
            required = false
        )
        page: Int = 0,
        @RequestParam
        @Parameter(
            name = "size",
            description = "Page size",
            required = false
        )
        size: Int = 10,
        assembler: PagedResourcesAssembler<TicketDto>,
    ): PagedModel<EntityModel<TicketDto>> {
        val ticketList =  ticketService.list(
            query = ListTicketQuery(
                categoryId = categoryId, eventId = eventId,
                title = title, page = page, size = size))
        return assembler.toModel(ticketList)
    }

    @GetMapping("/{ticketId}")
    @Operation(
        summary = "Get a ticket by ID",
        description = "Returns a ticket by ID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Ticket",
                content = [Content(schema = Schema(implementation = TicketDto::class))]
            )
        ]
    )
    fun load(
        @PathVariable
        @Parameter(
            name = "categoryId",
            description = "Category ID",
            required = true
        )
        categoryId: Long,
        @PathVariable
        @Parameter(
            name = "eventId",
            description = "Event ID",
            required = true
        )
        eventId: Long,
        @PathVariable
        @Parameter(
            name = "ticketId",
            description = "Ticket ID",
            required = true
        )
        ticketId: Long
    ): TicketDto = ticketService.load(
        categoryId = categoryId, eventId = eventId, ticketId = ticketId)

    @PostMapping
    @Operation(
        summary = "Create a new ticket",
        description = "Creates a new ticket",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Ticket created",
                content = [Content(schema = Schema(implementation = Long::class))]
            )
        ]
    )
    fun create(
        @PathVariable
        @Parameter(
            name = "categoryId",
            description = "Category ID",
            required = true
        )
        categoryId: Long,
        @PathVariable
        @Parameter(
            name = "eventId",
            description = "Event ID",
            required = true
        )
        eventId: Long,
        @RequestBody
        @Parameter(
            name = "command",
            description = "Ticket creation command",
            required = true
        )
        command: CreateTicketCommand
    ): Long = ticketService.create(categoryId, eventId, command)

    @PutMapping("/{ticketId}")
    @Operation(
        summary = "Update a ticket",
        description = "Updates a ticket",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Ticket updated"
            )
        ]
    )
    fun update(
        @PathVariable
        @Parameter(
            name = "categoryId",
            description = "Category ID",
            required = true
        )
        categoryId: Long,
        @PathVariable
        @Parameter(
            name = "eventId",
            description = "Event ID",
            required = true
        )
        eventId: Long,
        @PathVariable
        @Parameter(
            name = "ticketId",
            description = "Ticket ID",
            required = true
        )
        ticketId: Long,
        @RequestBody
        @Parameter(
            name = "command",
            description = "Ticket update command",
            required = true
        )
        command: UpdateTicketCommand
    ) = ticketService.update(categoryId, eventId, ticketId, command)
}