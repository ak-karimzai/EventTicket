package com.akkarimzai.eventticket.models.order

import java.time.LocalDateTime

data class OrderDto(val id: Long,
                    val items: List<OrderItemDto>,
                    val orderPlaced: LocalDateTime,
                    val orderPaid: Boolean)
