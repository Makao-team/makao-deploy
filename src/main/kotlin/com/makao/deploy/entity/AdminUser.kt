package com.makao.deploy.entity

import com.makao.deploy.entity.base.Auditable
import jakarta.persistence.*

@Entity
class AdminUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var name: String,

    @Column(unique = true)
    val email: String,
    var password: String,
) : Auditable()