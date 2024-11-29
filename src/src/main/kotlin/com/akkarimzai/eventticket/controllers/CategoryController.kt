package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.annotations.LogExecutionTime
import com.akkarimzai.eventticket.models.category.CategoryDto
import com.akkarimzai.eventticket.models.category.CreateCategoryCommand
import com.akkarimzai.eventticket.models.category.ListCategoryQuery
import com.akkarimzai.eventticket.models.category.UpdateCategoryCommand
import com.akkarimzai.eventticket.services.impl.CategoryService
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/categories"])
@LogExecutionTime
class CategoryController(private val categoryService: CategoryService) {
    @GetMapping
    fun list(
        @RequestParam title: String? = null,
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        assembler: PagedResourcesAssembler<CategoryDto>
    ): PagedModel<EntityModel<CategoryDto>> {
        val categoryList = categoryService.list(
            query = ListCategoryQuery(
                title = title, page = page, size = size))
        return assembler.toModel(categoryList)
    }

    @PostMapping
    fun create(
        @RequestBody command: CreateCategoryCommand
    ): Long = categoryService.create(command)

    @PutMapping("/{categoryId}")
    fun update(
        @PathVariable categoryId: Long, @RequestBody command: UpdateCategoryCommand
    ) = categoryService.update(categoryId, command)

    @GetMapping("/{categoryId}")
    fun load(@PathVariable categoryId: Long
    ): CategoryDto = categoryService.load(categoryId)
}