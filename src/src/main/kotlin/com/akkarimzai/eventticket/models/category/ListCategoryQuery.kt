package com.akkarimzai.eventticket.models.category

import com.akkarimzai.eventticket.models.common.AbstractValidatableCQ
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThanOrEqualTo
import org.valiktor.functions.isLessThanOrEqualTo
import org.valiktor.validate

data class ListCategoryQuery(
    val title: String?,
    val page: Int,
    val size: Int
): AbstractValidatableCQ() {
    override fun dataValidator() {
        validate(this) {
            title?.let {
                validate(ListCategoryQuery::title)
                    .hasSize(min = 1, max = 126)
            }

            validate(ListCategoryQuery::page)
                .isGreaterThanOrEqualTo(0)

            validate(ListCategoryQuery::size)
                .isGreaterThanOrEqualTo(1)
                .isLessThanOrEqualTo(20)
        }
    }
}
