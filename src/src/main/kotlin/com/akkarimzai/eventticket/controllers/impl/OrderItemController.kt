package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.annotations.LogExecutionTime
import com.akkarimzai.eventticket.models.orderItem.CreateOrderItemCommand
import com.akkarimzai.eventticket.models.orderItem.ListOrderItemQuery
import com.akkarimzai.eventticket.models.orderItem.OrderItemDto
import com.akkarimzai.eventticket.models.orderItem.UpdateOrderItemCommand
import com.akkarimzai.eventticket.services.impl.OrderItemService
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.*

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/orders/{orderId}/items"])
@Tag(name = "Order Item API", description = "API for managing order items")
@LogExecutionTime
class OrderItemController(private val orderItemService: OrderItemService) {

    @GetMapping
    @Operation(
        summary = "Get a list of order items",
        description = "Returns a list of order items",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "List of order items",
                content = [Content(schema = Schema(implementation = PagedModel::class))]
            )
        ]
    )
    fun list(
        @PathVariable
        @Parameter(
            name = "orderId",
            description = "Order ID",
            required = true
        )
        orderId: Long,
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
        assembler: PagedResourcesAssembler<OrderItemDto>
    ): PagedModel<EntityModel<OrderItemDto>> {
        val items = orderItemService.list(
            query = ListOrderItemQuery(
                orderId = orderId, page = page, size = size))
        return assembler.toModel(items)
    }

    @GetMapping("/{orderItemId}")
    @Operation(
        summary = "Get an order item by ID",
        description = "Returns an order item by ID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Order item",
                content = [Content(schema = Schema(implementation = OrderItemDto::class))]
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
        orderId: Long,
        @PathVariable
        @Parameter(
            name = "orderItemId",
            description = "Order item ID",
            required = true
        )
        orderItemId: Long
    ): OrderItemDto = orderItemService.load(orderId, orderItemId)

    @PostMapping
    @Operation(
        summary = "Create a new order item",
        description = "Creates a new order item",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Order item created",
                content = [Content(schema = Schema(implementation = Long::class))]
            )
        ]
    )
    fun create(
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
            description = "Order item creation command",
            required = true
        )
        command: CreateOrderItemCommand
    ): Long = orderItemService.create(orderId, command = command)

    @PutMapping
    @Operation(
        summary = "Update an order item",
        description = "Updates an order item",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Order item updated"
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
            description = "Order item update command",
            required = true
        )
        command: UpdateOrderItemCommand
    ) = orderItemService.update(orderId, command)

    @DeleteMapping("/{orderItemId}")
    @Operation(
        summary = "Delete an order item",
        description = "Deletes an order item",
        responses = [
            ApiResponse(
                responseCode = "204",
                description = "Order item deleted"
            )
        ]
    )
    fun delete(
        @PathVariable
        @Parameter(
            name = "orderId",
            description = "Order ID",
            required = true
        )
        orderId: Long,
        @PathVariable
        @Parameter(
            name = "orderItemId",
            description = "Order item ID",
            required = true
        )
        orderItemId: Long
    ) = orderItemService.delete(orderId, orderItemId)
}