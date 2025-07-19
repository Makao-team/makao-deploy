package com.makao.deploy.entity

import com.makao.deploy.entity.base.Auditable
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Entity
class AiProject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,

    var description: String,

    @Enumerated(EnumType.STRING)
    var status: AiProjectStatus = AiProjectStatus.INACTIVE,

    var createdBy: Long
) : Auditable()

enum class AiProjectStatus(val value: String) {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE")
}

@Repository
interface AiProjectRepository : JpaRepository<AiProject, Long> {
    fun findByCreatedBy(createdBy: Long): List<AiProject>
}