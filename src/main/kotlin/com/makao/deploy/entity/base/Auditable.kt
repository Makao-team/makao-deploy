package com.makao.deploy.entity.base

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@SQLRestriction("is_archived = false")
@EntityListeners(AuditingEntityListener::class)
abstract class Auditable {
    @CreatedDate
    @Column(updatable = false)
    open var createdAt: LocalDateTime? = null

    open var isArchived: Boolean = false

    fun archive() {
        isArchived = true
    }
}
