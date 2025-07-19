package com.makao.deploy.entity

import com.makao.deploy.entity.base.Auditable
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Entity
@Table(name = "prompt")
class Prompt(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "project_id")
    var projectId: Long,

    var content: String,

    var version: Int = 1,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @ElementCollection
    @CollectionTable(
        name = "prompt_variables",
        joinColumns = [JoinColumn(name = "prompt_id")]
    )
    @Column(name = "variable_name")
    var variables: MutableList<String> = mutableListOf()

) : Auditable()

@Repository
interface PromptRepository : JpaRepository<Prompt, Long> {
    fun findByProjectIdAndIsArchivedFalse(projectId: Long): List<Prompt>
    fun findByProjectIdAndVersionAndIsArchivedFalse(projectId: Long, version: Int): Prompt?
    fun findTopByProjectIdAndIsArchivedFalseOrderByVersionDesc(projectId: Long): Prompt?
}