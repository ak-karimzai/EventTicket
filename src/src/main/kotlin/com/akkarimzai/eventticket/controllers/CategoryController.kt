package com.akkarimzai.eventticket.controllers

import com.akkarimzai.eventticket.annotations.LogExecutionTime
import com.akkarimzai.eventticket.controllers.middlewares.ErrorResponse
import com.akkarimzai.eventticket.models.category.CategoryDto
import com.akkarimzai.eventticket.models.category.CreateCategoryCommand
import com.akkarimzai.eventticket.models.category.ListCategoryQuery
import com.akkarimzai.eventticket.models.category.UpdateCategoryCommand
import com.akkarimzai.eventticket.services.impl.CategoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/categories"])
@Tag(name = "Category API", description = "API for managing categories")
//@LogExecutionTime
class CategoryController(private val categoryService: CategoryService) {
    @GetMapping
    @Operation(
        summary = "Get a list of categories",
        description = "Returns a list of categories",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "List of categories",
                content = [Content(schema = Schema(implementation = PagedModel::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid query parameters",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun list(
        @Parameter(
            name = "title",
            description = "Filter by title",
            required = false
        )
        @RequestParam title: String? = null,
        @Parameter(
            name = "page",
            description = "Page number",
            required = false
        )
        @RequestParam page: Int = 0,
        @Parameter(
            name = "size",
            description = "Page size",
            required = false
        )
        @RequestParam size: Int = 10,
        assembler: PagedResourcesAssembler<CategoryDto>
    ): PagedModel<EntityModel<CategoryDto>> {
        val categoryList = categoryService.list(
            query = ListCategoryQuery(
                title = title, page = page, size = size)
        )
        return assembler.toModel(categoryList)
    }

    @PostMapping
    @Operation(
        summary = "Create a new category",
        description = "Creates a new category",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Category created",
                content = [Content(schema = Schema(implementation = Long::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Permission denied",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun create(
        @RequestBody
        @Parameter(
            name = "command",
            description = "Category creation command",
            required = true
        )
        command: CreateCategoryCommand
    ): Long = categoryService.create(command)

    @PutMapping("/{categoryId}")
    @Operation(
        summary = "Update a category",
        description = "Updates a category",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Category updated"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Permission denied",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun update(
        @PathVariable
        @Parameter(
            name = "categoryId",
            description = "Category ID",
            required = true
        )
        categoryId: Long,
        @RequestBody
        @Parameter(
            name = "command",
            description = "Category update command",
            required = true
        )
        command: UpdateCategoryCommand
    ) = categoryService.update(categoryId, command)

    @GetMapping("/{categoryId}")
    @Operation(
        summary = "Get a category by ID",
        description = "Returns a category by ID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Category",
                content = [Content(schema = Schema(implementation = CategoryDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Category not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun load(
        @PathVariable
        @Parameter(
            name = "categoryId",
            description = "Category ID",
            required = true
        )
        categoryId: Long
    ): CategoryDto = categoryService.load(categoryId)
}