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

@RestController
@RequestMapping(value = ["/api/v1/orders/{orderId}/items"])
@LogExecutionTime
class OrderItemController(private val orderItemService: OrderItemService) {
    fun list(
        @PathVariable orderId: Long,
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        assembler: PagedResourcesAssembler<OrderItemDto>
    ): PagedModel<EntityModel<OrderItemDto>> {
        val items = orderItemService.list(
            query = ListOrderItemQuery(
                orderId = orderId, page = page, size = size))
        return assembler.toModel(items)
    }

    @GetMapping("/{orderItemId}")
    fun load(
        @PathVariable orderId: Long,
        @PathVariable orderItemId: Long
    ): OrderItemDto = orderItemService.load(orderId, orderItemId)

    @PostMapping
    fun create(
        @PathVariable orderId: Long, @RequestBody command: CreateOrderItemCommand
    ): Long = orderItemService.create(orderId, command = command)

    @PutMapping
    fun update(
        @PathVariable orderId: Long, @RequestBody command: UpdateOrderItemCommand
    ) = orderItemService.update(orderId, command)

    @DeleteMapping("/{orderItemId}")
    fun delete(
        @PathVariable orderId: Long,
        @PathVariable orderItemId: Long
    ) = orderItemService.delete(orderId, orderItemId)
}