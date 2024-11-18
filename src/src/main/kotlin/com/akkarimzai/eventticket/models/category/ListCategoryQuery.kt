package com.akkarimzai.eventticket.models.category

import jakarta.validation.constraints.Min
import org.hibernate.validator.constraints.Length

data class ListCategoryQuery(
    @Length(min = 1, max = 126)
    val title: String?,
    @Min(0)
    val page: Long,
    @Min(1)
    val size: Long
)
