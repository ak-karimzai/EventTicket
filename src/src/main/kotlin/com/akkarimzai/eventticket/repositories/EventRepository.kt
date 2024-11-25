package com.akkarimzai.eventticket.repositories

import com.akkarimzai.eventticket.entities.Event
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    fun findAll(specification: Specification<Event>, pageable: Pageable): Page<Event>
}