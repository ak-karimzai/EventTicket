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
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/orders"])
@LogExecutionTime
class OrderController(private val orderService: OrderService) {
    fun list(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        assembler: PagedResourcesAssembler<OrderDto>,
        ): PagedModel<EntityModel<OrderDto>> {
        val orderList = orderService.list(
            query = ListOrderQuery(
                page = page, size = size))
        return assembler.toModel(orderList)
    }

    @GetMapping("/{orderId}")
    fun load(
        @PathVariable orderId: Long
    ): OrderDto = orderService.load(orderId)

    @PostMapping
    fun create(
        @RequestBody command: CreateOrderCommand
    ): Long = orderService.create(command)

    @PutMapping("/{orderId}")
    fun update(
        @PathVariable orderId: Long,
        @RequestBody command: UpdateOrderCommand
    ) = orderService.update(orderId, command)
}