package com.akkarimzai.eventticket.models.event

import jakarta.validation.constraints.Min
import java.time.LocalDateTime

data class ListEventQuery(val title: String?,
    val artist: String?,
    val from: LocalDateTime?,
    val to: LocalDateTime?,
    @Min(0)
    val page: Int,
    @Min(1)
    val size: Int)
