package com.akkarimzai.eventticket.profiles

import com.akkarimzai.eventticket.entities.Category
import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.models.category.CategoryDto
import com.akkarimzai.eventticket.models.category.CreateCategoryCommand
import com.akkarimzai.eventticket.models.category.UpdateCategoryCommand
import java.time.LocalDateTime

fun CreateCategoryCommand.toCategory(user: User): Category {
    return Category(
        title = this.title,
    ).also {
        it.createdBy = user
        it.createdDate = LocalDateTime.now()
    }
}

fun UpdateCategoryCommand.toCategory(user: User, category: Category): Category {
    this.title?.let { category.title = this.title }

    return category.also {
        it.lastModifiedDate = LocalDateTime.now()
        it.lastModifiedBy = user
    }
}

fun Category.toDto(): CategoryDto {
    return CategoryDto(
        id = this.id!!,
        title = this.title
    )
}
