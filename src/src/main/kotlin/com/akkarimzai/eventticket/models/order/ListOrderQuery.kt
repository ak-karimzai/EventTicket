package com.akkarimzai.eventticket.models.order

import jakarta.validation.constraints.Min

data class ListOrderQuery(
    @Min(0)
    val page: Long,
    @Min(1)
    val size: Long)
