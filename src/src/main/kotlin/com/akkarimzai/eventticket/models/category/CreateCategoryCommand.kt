package com.akkarimzai.eventticket.models.category

import com.akkarimzai.eventticket.models.event.CreateEventCommand
import jakarta.validation.constraints.Size

data class CreateCategoryCommand(
    @Size(min = 3, max = 256)
    val title: String,
    val events: List<CreateEventCommand>?
)