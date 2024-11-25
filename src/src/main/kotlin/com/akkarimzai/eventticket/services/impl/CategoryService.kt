package com.akkarimzai.eventticket.services.impl

import com.akkarimzai.eventticket.annotations.Validate
import com.akkarimzai.eventticket.entities.Category
import com.akkarimzai.eventticket.exceptions.NotFoundException
import com.akkarimzai.eventticket.models.category.CategoryDto
import com.akkarimzai.eventticket.models.category.CreateCategoryCommand
import com.akkarimzai.eventticket.models.category.ListCategoryQuery
import com.akkarimzai.eventticket.models.category.UpdateCategoryCommand
import com.akkarimzai.eventticket.profiles.toCategory
import com.akkarimzai.eventticket.profiles.toDto
import com.akkarimzai.eventticket.repositories.CategoryRepository
import com.akkarimzai.eventticket.repositories.specs.CategorySpecification
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
@Validate
class CategoryService(private val repository: CategoryRepository) {
    private val logger = KotlinLogging.logger {}

    fun load(categoryId: Long): CategoryDto {
        logger.info("Loading category with id: $categoryId")

        return loadCategory(categoryId).toDto()
    }

    fun create(command: CreateCategoryCommand): Long {
        logger.info("Request create category: $command")

        val category = command.toCategory()
        return repository.save(category).id!!.also {
            logger.info { "Successfully created new category: $it" }
        }
    }

    fun update(categoryId: Long, command: UpdateCategoryCommand) {
        logger.info("Request update category: $command")

        val category = loadCategory(categoryId)
        val categoryToUpdate = command.toCategory(category)

        repository.save(categoryToUpdate).also {
            logger.info { "Successfully updated new category: $it" }
        }
    }

    fun list(query: ListCategoryQuery): Page<CategoryDto> {
        logger.info("Request list category: $query")

        return repository.findAll(CategorySpecification.buildSpecification(
            title = query.title),
            pageable = PageRequest.of(query.page, query.size)
        ).map { it.toDto() }
    }

    private fun loadCategory(categoryId: Long): Category {
        val category = repository.findById(categoryId).orElseThrow {
            logger.debug("Category with id: $categoryId not found")
            NotFoundException("category", categoryId)
        }
        return category;
    }
}