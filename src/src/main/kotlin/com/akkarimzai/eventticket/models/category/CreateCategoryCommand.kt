package com.akkarimzai.eventticket.models.category

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import com.akkarimzai.eventticket.models.event.CreateEventCommand
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.validateForEach
import org.valiktor.validate

data class CreateCategoryCommand(
    val title: String,
    val events: List<CreateEventCommand>?
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