package com.akkarimzai.eventticket.models.category

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import com.akkarimzai.eventticket.models.event.CreateEventCommand
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.validateForEach
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Command to create a new category")
data class CreateCategoryCommand(
    @Schema(description = "Title of the category", example = "Example Category", required = true)
    @get:NotBlank
    @get:Size(min = 3, max = 256)
    val title: String,

    @Schema(description = "List of events to create with the category", example = "[{...}]", required = false)
    @get:Size(min = 0, max = 20)
    val events: List<CreateEventCommand>? = null
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            validate(CreateCategoryCommand::title)
                .isNotBlank()
                .hasSize(min = 3, max = 256)

            events?.let {
                validate(CreateCategoryCommand::events)
                    .hasSize(min = 0, max = 20)
                    .validateForEach { it.validate() }
            }
        }
    }
}