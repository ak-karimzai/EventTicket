package com.akkarimzai.eventticket.repositories

import com.akkarimzai.eventticket.entities.Ticket
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<Ticket, Long> {
    fun findAll(specification: Specification<Ticket>, pageable: Pageable): Page<Ticket>
}
