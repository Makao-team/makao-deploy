package com.makao.deploy.entity

import com.makao.deploy.entity.base.Auditable
import jakarta.persistence.*

@Entity
class AdminUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long,

    @Column(unique = true)
    private var email: String,
    private var password: String,
) : Auditable()