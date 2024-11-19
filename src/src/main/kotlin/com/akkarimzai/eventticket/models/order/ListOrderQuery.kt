package com.akkarimzai.eventticket.models.order

import jakarta.validation.constraints.Min

data class ListOrderQuery(
    @Min(0)
    val page: Int,
    @Min(1)
    val size: Int)
