package com.makao.deploy.entity

import com.makao.deploy.entity.base.Auditable
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Entity
@Table(name = "api_provider")
class ApiProvider(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,

    var apiKey: String,

    var baseUrl: String,

    var createdBy: Long

) : Auditable()

@Repository
interface ApiProviderRepository : JpaRepository<ApiProvider, Long> {
    fun findByCreatedByAndIsArchivedFalse(createdBy: Long): List<ApiProvider>
}