package com.akkarimzai.eventticket.models.category

import com.akkarimzai.eventticket.annotations.AtLeastOneNotNull
import jakarta.validation.constraints.Size

@AtLeastOneNotNull
data class UpdateCategoryCommand(
    @Size(min = 3, max = 256)
    val title: String?
)
