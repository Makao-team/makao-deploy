package com.makao.deploy.entity

import com.makao.deploy.entity.base.Auditable
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

class AdminUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long,
    private var email: String,
    private var password: String,
) : Auditable()