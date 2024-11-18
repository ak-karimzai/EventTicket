package com.akkarimzai.eventticket.models.category

data class CreateCategoryCommand(
    val title: String,
    val events: List<CreateCategoryCommand>?
)