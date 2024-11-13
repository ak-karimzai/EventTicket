package com.akkarimzai.eventticket.entities

import com.akkarimzai.eventticket.entities.common.AuditableEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "events")
class Event(
    id: Long? = null,

    @Column(nullable = false)
    var title: String,

    var artist: String,

    @Column(nullable = false)
    var date: LocalDateTime,

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var tickets: MutableList<Ticket> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    var category: Category,

    createdBy: User
): AuditableEntity(id, createdBy) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Event) return false
        if (!super.equals(other)) return false

        if (title != other.title) return false
        if (artist != other.artist) return false
        if (date != other.date) return false
        if (tickets != other.tickets) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + tickets.hashCode()
        return result
    }
}