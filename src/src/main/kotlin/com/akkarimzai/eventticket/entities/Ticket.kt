package com.akkarimzai.eventticket.entities

import com.akkarimzai.eventticket.entities.common.AuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "tickets")
class Ticket(
    id: Long? = null,
    @Column(nullable = false)
    var title: String,

    var description: String? = null,

    @Column(nullable = false)
    var price: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    var event: Event?,
): AuditableEntity(id)