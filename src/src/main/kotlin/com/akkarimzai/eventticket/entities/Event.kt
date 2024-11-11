package com.akkarimzai.eventticket.entities

import com.akkarimzai.eventticket.entities.common.AuditableEntity
import jakarta.persistence.Column
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "events")
class Event(
    id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var artist: String,

    @Column(nullable = false)
    var date: LocalDateTime,

    createdBy: User
): AuditableEntity(id, createdBy)