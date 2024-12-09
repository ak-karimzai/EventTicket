package com.akkarimzai.eventticket.entities

import com.akkarimzai.eventticket.entities.common.AuditableEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "events")
@SequenceGenerator(name = "event_seq", sequenceName = "event_seq", allocationSize = 1)
class Event(
    id: Long? = null,

    @Column(nullable = false)
    var title: String,

    var artist: String?,

    @Column(nullable = false)
    var date: LocalDateTime,

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var tickets: MutableList<Ticket> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    var category: Category,
): AuditableEntity(id)