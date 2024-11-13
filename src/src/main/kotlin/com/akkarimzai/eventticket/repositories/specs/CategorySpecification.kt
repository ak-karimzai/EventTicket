package com.akkarimzai.eventticket.repositories.specs

import com.akkarimzai.eventticket.entities.Category
import com.akkarimzai.eventticket.entities.Category_
import org.springframework.data.jpa.domain.Specification

object CategorySpecification {
    fun buildSpecification(title: String?): Specification<Category> {
        return Specification<Category> { root, _, criteriaBuilder ->
            title?.let {
                criteriaBuilder.like(root.get(Category_.title), "%${it}%")
            }
        }
    }
}