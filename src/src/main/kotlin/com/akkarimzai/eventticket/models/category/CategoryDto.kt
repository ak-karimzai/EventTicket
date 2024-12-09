package com.akkarimzai.eventticket.models.category

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Category data transfer object")
data class CategoryDto(
    @Schema(description = "Unique identifier of the category", example = "1")
    val id: Long,

    @Schema(description = "Title of the category", example = "Example Category")
    val title: String
)
