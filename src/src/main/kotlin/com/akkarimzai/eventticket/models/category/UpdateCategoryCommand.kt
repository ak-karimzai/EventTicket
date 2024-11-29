package com.akkarimzai.eventticket.models.category

import com.akkarimzai.eventticket.exceptions.BadRequestException
import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

data class UpdateCategoryCommand(
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