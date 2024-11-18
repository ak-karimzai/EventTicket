package com.akkarimzai.eventticket.entities

import com.akkarimzai.eventticket.entities.common.AuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "categories")
class Category(
    id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var events: MutableList<Event> = mutableListOf(),

    createdBy: User,
): AuditableEntity(id, createdBy)