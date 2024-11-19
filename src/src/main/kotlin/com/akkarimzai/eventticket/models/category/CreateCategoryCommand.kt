package com.akkarimzai.eventticket.models.category

import com.akkarimzai.eventticket.models.event.CreateEventCommand
import org.hibernate.validator.constraints.Length

data class CreateCategoryCommand(
    @Length(min = 3, max = 256)
    val title: String,
    val events: List<CreateEventCommand>?
)