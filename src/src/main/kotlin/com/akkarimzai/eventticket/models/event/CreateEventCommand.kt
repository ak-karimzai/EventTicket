package com.akkarimzai.eventticket.models.event

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreateEventCommand(
    @NotBlank @Size(min = 3, max = 256)
    val title: String,

    @NotBlank @Size(min = 3, max = 256)
    val artist: String?,

    @Future
    val date: LocalDateTime,

    val tickets: List<EventTicketDto>
)