package com.akkarimzai.eventticket.models.event

import java.time.LocalDateTime

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Event data transfer object")
data class EventDto(
    @Schema(description = "Unique identifier of the event", example = "1", required = true)
    val id: Long,

    @Schema(description = "Title of the event", example = "Example Event", required = true)
    val title: String,

    @Schema(description = "Artist performing at the event", example = "Example Artist", required = false)
    val artist: String?,

    @Schema(description = "Date and time of the event", example = "2022-01-01T12:00:00", required = true)
    val date: LocalDateTime
)