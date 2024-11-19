package com.akkarimzai.eventticket.profiles

import com.akkarimzai.eventticket.entities.Category
import com.akkarimzai.eventticket.models.category.CreateCategoryCommand

fun CreateCategoryCommand.toCategory(): Category {
    return Category(
        title = this.title,
    ).also { category ->
            category.events = this.events?.map { it.toEvent(category)
        }?.toMutableList() ?: mutableListOf()
    }
}