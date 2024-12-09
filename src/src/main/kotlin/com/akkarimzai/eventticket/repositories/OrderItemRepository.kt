package com.akkarimzai.eventticket.repositories

import com.akkarimzai.eventticket.entities.OrderItem
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository : JpaRepository<OrderItem, Long> {
    fun findAllByOrderId(orderId: Long, pageable: Pageable): Page<OrderItem>
    fun findAllByOrderId(orderId: Long): List<OrderItem>
}