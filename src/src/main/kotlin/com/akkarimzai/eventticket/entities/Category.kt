package com.akkarimzai.eventticket.entities

import com.akkarimzai.eventticket.entities.common.AuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "categories")
@SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1)
class Category(
    id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var events: MutableList<Event> = mutableListOf(),

): AuditableEntity(id)