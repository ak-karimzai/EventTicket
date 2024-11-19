package com.akkarimzai.eventticket.models.event

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class ListEventQuery(
    @Size(min = 3, max = 256)
    val title: String?,
    @Size(min = 3, max = 256)
    val artist: String?,
    val from: LocalDateTime?,
    val to: LocalDateTime?,
    @Min(0)
    val page: Int,
    @Min(1)
    val size: Int)
