package com.akkarimzai.eventticket.entities

import com.akkarimzai.eventticket.entities.common.Entity
import jakarta.persistence.*
import java.time.LocalDateTime

@jakarta.persistence.Entity
@Table(name = "orders")
class Order(
    id: Long?,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(name = "order_placed", nullable = false)
    var orderPlaced: LocalDateTime = LocalDateTime.now(),

    @Column(name = "order_paid", nullable = false)
    var orderPaid: Boolean = false,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var items: MutableList<OrderItem> = mutableListOf()
): Entity(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Order) return false
        if (!super.equals(other)) return false

        if (user != other.user) return false
        if (orderPlaced != other.orderPlaced) return false
        if (orderPaid != other.orderPaid) return false
        if (items != other.items) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + orderPlaced.hashCode()
        result = 31 * result + orderPaid.hashCode()
        result = 31 * result + items.hashCode()
        return result
    }
}