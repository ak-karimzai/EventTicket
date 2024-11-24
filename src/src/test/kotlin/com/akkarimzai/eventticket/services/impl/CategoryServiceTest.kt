package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.entities.Category
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.category.CreateCategoryCommand
import com.akkarimzai.eventticket.models.category.ListCategoryQuery
import com.akkarimzai.eventticket.models.category.UpdateCategoryCommand
import com.akkarimzai.eventticket.profiles.toDto
import com.akkarimzai.eventticket.repositories.CategoryRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

class CategoryServiceTest : FunSpec({
    val repository = mockk<CategoryRepository>()
    val service = CategoryService(repository)

    test("load should return category DTO when category exists") {
        // Arrange
        val categoryId = 1L
        val category = Category(id = categoryId, title = "Test Category")
        every { repository.findById(categoryId) } returns Optional.of(category)

        // Act
        val result = service.load(categoryId)

        // Assert
        result shouldBe category.toDto()
    }

    test("load should throw NotFoundException when category does not exist") {
        // Arrange
        val categoryId = 1L
        every { repository.findById(categoryId) } returns Optional.empty()

        // Act && Assert
        shouldThrow<NotFoundException> {
            service.load(categoryId)
        }
    }

    test("create should save a new category and return its ID") {
        // Arrange
        val command = CreateCategoryCommand(title = "New Category", null)
        val savedCategory = Category(id = 1L, title = command.title)

        every { repository.save(any()) } returns savedCategory

        // Act
        val result = service.create(command)

        // Assert
        result shouldBe 1L
    }

    test("update should save updated category") {
        // Arrange
        val categoryId = 1L
        val existingCategory = Category(id = categoryId, title = "Old Title")
        val command = UpdateCategoryCommand(title = "New Title")
        val updatedCategory = Category(id = categoryId, title = command.title!!)

        every { repository.findById(categoryId) } returns Optional.of(existingCategory)
        every { repository.save(any()) } returns updatedCategory

        // Act && Assert
        service.update(categoryId, command)
    }

    test("update should throw NotFoundException when category does not exist") {
        // Arrange
        val categoryId = 1L
        val command = UpdateCategoryCommand(title = "New Title")

        every { repository.findById(categoryId) } returns Optional.empty()

        // Act && Assert
        shouldThrow<NotFoundException> {
            service.update(categoryId, command)
        }
    }

    test("list should return paginated category DTOs") {
        // Arrange
        val query = ListCategoryQuery(title = "Category", page = 0, size = 10)
        val categories = listOf(Category(id = 1L, title = "Category"))
        val page = PageImpl(categories)

        every {
            repository.findAll(any(), PageRequest.of(query.page, query.size))
        } returns page

        // Act
        val result = service.list(query)

        // Assert
        result.content.size shouldBe 1
        result.content[0] shouldBe categories[0].toDto()
    }

    test("list should return an empty page when no categories match") {
        // Arrange
        val query = ListCategoryQuery(title = "Nonexistent", page = 0, size = 10)
        val emptyPage: Page<Category> = PageImpl(emptyList())

        every {
            repository.findAll(any(), PageRequest.of(query.page, query.size))
        } returns emptyPage

        // Act
        val result = service.list(query)

        // Assert
        result.content.shouldBeEmpty()
    }
})
