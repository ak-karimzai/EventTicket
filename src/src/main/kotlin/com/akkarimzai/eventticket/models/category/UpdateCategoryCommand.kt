package com.akkarimzai.eventticket.models.category

import com.akkarimzai.eventticket.exceptions.BadRequestException
import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Command to update a category")
data class UpdateCategoryCommand(
    @Schema(description = "New title of the category", example = "Example Category", required = false)
    @NotBlank
    @Size(min = 3, max = 256)
    val title: String?
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            var count = 0
            title?.let {
                validate(UpdateCategoryCommand::title)
                    .isNotBlank()
                    .hasSize(min = 3, max = 256)
                count++
            }
            if (count == 0) {
                throw BadRequestException("Nothing to update!")
            }
        }
    }
}