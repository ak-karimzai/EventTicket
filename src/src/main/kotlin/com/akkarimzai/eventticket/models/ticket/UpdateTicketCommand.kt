package com.akkarimzai.eventticket.models.ticket

import com.akkarimzai.eventticket.annotations.AtLeastOneNotNull
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

@AtLeastOneNotNull
class UpdateTicketCommand(
    @NotBlank @Length(min = 3, max = 256)
    val title: String?,

    @NotBlank @Length(min = 3, max = 256)
    val description: String?,

    @NotBlank @DecimalMin("0.0", inclusive = false)
    val price: Double?,
)