package com.akkarimzai.eventticket.repositories

import com.akkarimzai.eventticket.entities.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    fun findAllByUserId(userId: Long, pageable: Pageable): Page<Order>
}