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
import com.akkarimzai.eventticket.profiles.toEvent
import com.akkarimzai.eventticket.profiles.toTicket
import com.akkarimzai.eventticket.repositories.CategoryRepository
import com.akkarimzai.eventticket.repositories.EventRepository
import com.akkarimzai.eventticket.repositories.TicketRepository
import com.akkarimzai.eventticket.repositories.specs.CategorySpecification
import com.akkarimzai.eventticket.services.AuthService
import jakarta.transaction.Transactional
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
@Validate
open class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val eventRepository: EventRepository,
    private val ticketRepository: TicketRepository,
    private val authService: AuthService
) {
    private val logger = KotlinLogging.logger {}

    fun load(categoryId: Long): CategoryDto {
        logger.info("Loading category with id: $categoryId")

        return loadCategory(categoryId).toDto()
    }

    @Transactional
    open fun create(command: CreateCategoryCommand): Long {
        logger.info("Request create category: $command")

        val user = authService.currentUser()
        val category = command.toCategory(user)
        val savedCategory = categoryRepository.save(category)
        logger.info("Category created with ID: ${category.id}")

        command.events?.forEach { eventCommand ->
            logger.info("Processing event: ${eventCommand.title}")

            val event = eventCommand.toEvent(user, savedCategory)
            logger.info("Creating event: ${event.title} for category: ${savedCategory.title}")
            eventRepository.save(event)
            logger.info("Event created with ID: ${event.id}")

            val tickets = eventCommand.tickets.map { it.toTicket(user, event) }
            logger.info("Creating ${tickets.size} tickets for event: ${event.title}")
            ticketRepository.saveAll(tickets)
            logger.info("${tickets.size} tickets created for event: ${event.title}")
        }

        logger.info("Category creation complete with ID: ${savedCategory.id}")
        return savedCategory.id!!
    }

    fun update(categoryId: Long, command: UpdateCategoryCommand) {
        logger.info("Request update category: $command")

        val user = authService.currentUser()
        val category = loadCategory(categoryId)
        val categoryToUpdate = command.toCategory(user, category)

        categoryRepository.save(categoryToUpdate).also {
            logger.info { "Successfully updated category: $it" }
        }
    }

    fun list(query: ListCategoryQuery): Page<CategoryDto> {
        logger.info("Request list category: $query")

        return categoryRepository.findAll(CategorySpecification.buildSpecification(
            title = query.title),
            pageable = PageRequest.of(query.page, query.size)
        ).map { it.toDto() }
    }

    private fun loadCategory(categoryId: Long): Category {
        val category = categoryRepository.findById(categoryId).orElseThrow {
            logger.debug("Category with id: $categoryId not found")
            NotFoundException("category", categoryId)
        }
        return category;
    }
}