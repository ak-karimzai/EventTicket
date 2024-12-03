package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.annotations.LogExecutionTime
import com.akkarimzai.eventticket.controllers.middlewares.ErrorResponse
import com.akkarimzai.eventticket.models.order.CreateOrderCommand
import com.akkarimzai.eventticket.models.order.ListOrderQuery
import com.akkarimzai.eventticket.models.order.OrderDto
import com.akkarimzai.eventticket.models.order.UpdateOrderCommand
import com.akkarimzai.eventticket.services.impl.OrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/orders"])
@Tag(name = "Order API", description = "API for managing orders")
@LogExecutionTime
class OrderController(private val orderService: OrderService) {
    @GetMapping
    @Operation(
        summary = "Get a list of orders",
        description = "Returns a list of orders",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "List of orders",
                content = [Content(schema = Schema(implementation = PagedModel::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid query parameters",
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    fun list(
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
        assembler: PagedResourcesAssembler<OrderDto>,
    ): PagedModel<EntityModel<OrderDto>> {
        val orderList = orderService.list(
            query = ListOrderQuery(
                page = page, size = size))
        return assembler.toModel(orderList)
    }

    @GetMapping("/{orderId}")
    @Operation(
        summary = "Get an order by ID",
        description = "Returns an order by ID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Order",
                content = [Content(schema = Schema(implementation = OrderDto::class))]
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    fun load(
        @PathVariable
        @Parameter(
            name = "orderId",
            description = "Order ID",
            required = true
        )
        orderId: Long
    ): OrderDto = orderService.load(orderId)

    @PostMapping
    @Operation(
        summary = "Create a new order",
        description = "Creates a new order",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Order created",
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun create(
        @RequestBody
        @Parameter(
            name = "command",
            description = "Order creation command",
            required = true
        )
        command: CreateOrderCommand
    ): Long = orderService.create(command)

    @PutMapping("/{orderId}")
    @Operation(
        summary = "Update an order",
        description = "Updates an order",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Order updated"
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun update(
        @PathVariable
        @Parameter(
            name = "orderId",
            description = "Order ID",
            required = true
        )
        orderId: Long,
        @RequestBody
        @Parameter(
            name = "command",
            description = "Order update command",
            required = true
        )
        command: UpdateOrderCommand
    ) = orderService.update(orderId, command)
}