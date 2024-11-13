package com.akkarimzai.eventticket.entities.common

import com.akkarimzai.eventticket.entities.User
import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
abstract class AuditableEntity(
    id: Long?,
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: User,

    @Column(name = "created_date", nullable = false)
    var createdDate: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    var lastModifiedBy: User? = null,

    @Column(name = "last_modified_date")
    var lastModifiedDate: LocalDateTime? = null
): Entity(id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AuditableEntity

        if (createdBy != other.createdBy) return false
        if (createdDate != other.createdDate) return false
        if (lastModifiedBy != other.lastModifiedBy) return false
        if (lastModifiedDate != other.lastModifiedDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = createdBy.hashCode()
        result = 31 * result + createdDate.hashCode()
        result = 31 * result + lastModifiedBy.hashCode()
        result = 31 * result + lastModifiedDate.hashCode()
        return result
    }
}