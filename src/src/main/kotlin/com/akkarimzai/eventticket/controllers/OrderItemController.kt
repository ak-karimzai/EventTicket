package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.annotations.LogExecutionTime
import com.akkarimzai.eventticket.models.order.OrderItemDto
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/orders/{orderId}/items"])
@LogExecutionTime
class OrderItemController {
    fun list(): PagedModel<EntityModel<OrderItemDto>> {
        TODO()
    }

    @GetMapping("/{orderItemId}")
    fun load(
        @PathVariable orderId: Long,
        @PathVariable orderItemId: Long
    ): EntityModel<OrderItemDto> = TODO()

    fun create(): Long = TODO()

    fun update(): Unit = TODO()

    fun delete(): Unit = TODO()
}