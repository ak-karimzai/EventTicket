package com.akkarimzai.eventticket.profiles

import com.akkarimzai.eventticket.entities.Category
import com.akkarimzai.eventticket.models.category.CategoryDto
import com.akkarimzai.eventticket.models.category.CreateCategoryCommand
import com.akkarimzai.eventticket.models.category.UpdateCategoryCommand

fun CreateCategoryCommand.toCategory(): Category {
    return Category(
        title = this.title,
    ).also { category ->
            category.events = this.events?.map { it.toEvent(category)
        }?.toMutableList() ?: mutableListOf()
    }
}

fun UpdateCategoryCommand.toCategory(category: Category): Category {
    return Category(
        title = this.title ?: category.title,
    )
}

fun Category.toDto(): CategoryDto {
    return CategoryDto(
        id = this.id!!,
        title = this.title
    )
}
