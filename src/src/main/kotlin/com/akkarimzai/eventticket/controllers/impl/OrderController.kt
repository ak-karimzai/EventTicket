package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.annotations.LogExecutionTime
import com.akkarimzai.eventticket.models.order.CreateOrderCommand
import com.akkarimzai.eventticket.models.order.ListOrderQuery
import com.akkarimzai.eventticket.models.order.OrderDto
import com.akkarimzai.eventticket.models.order.UpdateOrderCommand
import com.akkarimzai.eventticket.services.impl.OrderService
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
            )
        ]
    )
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
            )
        ]
    )
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
            )
        ]
    )
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
            )
        ]
    )
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