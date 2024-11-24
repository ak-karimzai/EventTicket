package com.akkarimzai.eventticket.repositories.configs

import com.akkarimzai.eventticket.entities.common.AuditableEntity
import com.akkarimzai.eventticket.services.impl.AuthServiceImpl
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AuditableEntityListener(@Lazy private val authService: AuthServiceImpl) {
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