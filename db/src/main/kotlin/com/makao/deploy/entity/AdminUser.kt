package com.makao.deploy.entity

import com.makao.deploy.entity.base.Auditable
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Entity
class AdminUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String,

    @Column(unique = true)
    val email: String,

    var password: String,

    @Enumerated(EnumType.STRING)
    var role: AdminUserRole = AdminUserRole.ADMIN,

    var isConfirmed: Boolean = false
) : Auditable()

enum class AdminUserRole(val value: String) {
    SUPER_ADMIN("SUPER_ADMIN"),
    ADMIN("ADMIN")
}

@Repository
interface AdminUserRepository : JpaRepository<AdminUser, Long> {
    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): AdminUser?
}