package com.akkarimzai.eventticket.entities

import com.akkarimzai.eventticket.entities.common.AuditableEntity
import jakarta.persistence.Column
import jakarta.persistence.Table

@Table(name = "categories")
class Category(
    id: Long? = null,

    @Column(nullable = false)
    var title: String,

    createdBy: User,
): AuditableEntity(id, createdBy) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Category) return false
        if (!super.equals(other)) return false

        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }
}