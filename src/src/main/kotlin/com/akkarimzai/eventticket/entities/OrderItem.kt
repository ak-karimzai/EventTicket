package com.akkarimzai.eventticket.entities

import com.akkarimzai.eventticket.entities.common.Entity
import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@jakarta.persistence.Entity
@Table(name = "order_items")
class OrderItem(
    id: Long?,

    @ManyToOne
    @JoinColumn(name = "order_id")
    var order: Order,

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    var ticket: Ticket,

    @Column(nullable = false)
    var amount: Int
): Entity(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrderItem) return false
        if (!super.equals(other)) return false

        if (order != other.order) return false
        if (ticket != other.ticket) return false
        if (amount != other.amount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + order.hashCode()
        result = 31 * result + ticket.hashCode()
        result = 31 * result + amount
        return result
    }
}