package com.akkarimzai.eventticket.controllers.impl

import com.akkarimzai.eventticket.annotations.LogExecutionTime
import com.akkarimzai.eventticket.controllers.middlewares.ErrorResponse
import com.akkarimzai.eventticket.models.event.CreateEventCommand
import com.akkarimzai.eventticket.models.event.EventDto
import com.akkarimzai.eventticket.models.event.ListEventQuery
import com.akkarimzai.eventticket.models.event.UpdateEventCommand
import com.akkarimzai.eventticket.services.impl.EventService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping(value = ["/api/v1/categories/{categoryId}/events"])
@Tag(name = "Event API", description = "API for managing events")
@LogExecutionTime
class EventController(private val eventService: EventService){
    @GetMapping
    @Operation(
        summary = "Get a list of events for a category",
        description = "Returns a list of events for a category",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "List of events",
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
        categoryId: Long,
        @RequestParam
        @Parameter(
            name = "title",
            description = "Filter by title",
            required = false
        )
        title: String? = null,
        @RequestParam
        @Parameter(
            name = "artist",
            description = "Filter by artist",
            required = false
        )
        artist: String? = null,
        @RequestParam
        @Parameter(
            name = "from",
            description = "Filter by start date",
            required = false
        )
        from: LocalDateTime? = null,
        @RequestParam
        @Parameter(
            name = "to",
            description = "Filter by end date",
            required = false
        )
        to: LocalDateTime? = null,
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
        assembler: PagedResourcesAssembler<EventDto>
    ): PagedModel<EntityModel<EventDto>> {
        val categoryList = eventService.list(
            query = ListEventQuery(
                categoryId = categoryId, title = title, artist = artist,
                from = from, to = to, page = page, size = size
            )
        )
        return assembler.toModel(categoryList)
    }

    @GetMapping("/{eventId}")
    @Operation(
        summary = "Get an event by ID",
        description = "Returns an event by ID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Event",
                content = [Content(schema = Schema(implementation = EventDto::class))]
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
    ): EventDto = eventService.load(
        categoryId = categoryId, eventId = eventId
    )

    @PostMapping
    @Operation(
        summary = "Create a new event",
        description = "Creates a new event",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Event created",
                content = [Content(schema = Schema(implementation = Long::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Permission denied",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
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
        @RequestBody
        @Parameter(
            name = "command",
            description = "Event creation command",
            required = true
        )
        command: CreateEventCommand
    ): Long = eventService.create(
        categoryId = categoryId, command = command
    )

    @PutMapping("/{eventId}")
    @Operation(
        summary = "Update an event",
        description = "Updates an event",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Event updated"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Permission denied",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
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
        @RequestBody
        @Parameter(
            name = "command",
            description = "Event update command",
            required = true
        )
        command: UpdateEventCommand
    ) = eventService.update(
        categoryId = categoryId, eventId = eventId,
        command = command
    )
}