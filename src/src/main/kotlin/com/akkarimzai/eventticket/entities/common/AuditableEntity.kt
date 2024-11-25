package com.akkarimzai.eventticket.entities.common

import com.akkarimzai.eventticket.entities.User
import com.akkarimzai.eventticket.repositories.configs.AuditableEntityListener
import jakarta.persistence.*
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditableEntityListener::class)
abstract class AuditableEntity(
    id: Long?,
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: User? = null,

    @Column(name = "created_date", nullable = false)
    var createdDate: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    var lastModifiedBy: User? = null,

    @Column(name = "last_modified_date")
    var lastModifiedDate: LocalDateTime? = null
): Entity(id)