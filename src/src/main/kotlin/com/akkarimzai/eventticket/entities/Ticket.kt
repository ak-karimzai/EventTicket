package com.akkarimzai.eventticket.entities

import com.akkarimzai.eventticket.entities.common.AuditableEntity
import jakarta.persistence.*

@jakarta.persistence.Entity
@Table(name = "tickets")
class Ticket(
    id: Long?,
    @Column(nullable = false)
    var title: String,

    var description: String? = null,

    @Column(nullable = false)
    var price: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    var event: Event,
    createdBy: User
): AuditableEntity(id, createdBy) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Ticket) return false
        if (!super.equals(other)) return false

        if (title != other.title) return false
        if (description != other.description) return false
        if (price != other.price) return false
        if (event != other.event) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + price.hashCode()
        result = 31 * result + event.hashCode()
        return result
    }
}