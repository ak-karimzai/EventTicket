package com.akkarimzai.eventticket.repositories

import com.akkarimzai.eventticket.entities.Category
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun findAll(specification: Specification<Category>, pageable: Pageable): Page<Category>
}