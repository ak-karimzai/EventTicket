package com.akkarimzai.eventticket.entities

import com.akkarimzai.eventticket.entities.common.Entity
import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@jakarta.persistence.Entity
@Table(name = "order_items")
class OrderItem(
    id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order?,

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    var ticket: Ticket,

    @Column(nullable = false)
    var amount: Int
): Entity(id)