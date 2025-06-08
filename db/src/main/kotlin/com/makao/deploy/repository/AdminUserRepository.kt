package com.makao.deploy.repository

import com.makao.deploy.entity.AdminUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminUserRepository : JpaRepository<AdminUser, Long> {
    fun existsByEmail(email: String): Boolean
    
    fun findByEmail(email: String): AdminUser?
} 