package com.akkarimzai.eventticket.models.category

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class ListCategoryQuery(
    @Size(min = 1, max = 126)
    val title: String?,
    @Min(0)
    val page: Int,
    @Min(1)
    val size: Int
)
