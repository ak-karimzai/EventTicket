package com.akkarimzai.eventticket.models.event

import com.akkarimzai.eventticket.annotations.AtLeastOneNotNull
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@AtLeastOneNotNull
class UpdateEventCommand(
    @NotBlank
    @Size(min = 3, max = 256)
    val title: String?,

    @NotBlank
    @Size(min = 3, max = 256)
    val artist: String?,

    @FutureOrPresent
    val date: LocalDateTime?
)