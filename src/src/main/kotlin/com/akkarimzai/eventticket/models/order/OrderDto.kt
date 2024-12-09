package com.akkarimzai.eventticket.models.order

import com.akkarimzai.eventticket.models.orderItem.OrderItemDto
import java.time.LocalDateTime

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Order data transfer object")
data class OrderDto(
    @Schema(description = "Unique identifier of the order", example = "1", required = true)
    val id: Long,

    @Schema(description = "List of order items", example = "[{...}]", required = true)
    val items: List<OrderItemDto>,

    @Schema(description = "Date and time the order was placed", example = "2022-01-01T12:00:00", required = true)
    val orderPlaced: LocalDateTime,

    @Schema(description = "Whether the order has been paid", example = "true", required = true)
    val orderPaid: Boolean
)