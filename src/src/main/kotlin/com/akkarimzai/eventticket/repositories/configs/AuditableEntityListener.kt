package com.akkarimzai.eventticket.repositories.configs

import com.akkarimzai.eventticket.entities.common.AuditableEntity
import com.akkarimzai.eventticket.services.impl.AuthService
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime


class AuditableEntityListener(private val authService: AuthService) {
    @PrePersist
    fun prePersist(entity: AuditableEntity) {
        entity.createdDate = LocalDateTime.now()
        entity.createdBy = authService.currentUser()
    }

    @PreUpdate
    fun preUpdate(entity: AuditableEntity) {
        entity.lastModifiedDate = LocalDateTime.now()
        entity.lastModifiedBy = authService.currentUser()
    }
}