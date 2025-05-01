package com.makao.deploy.entity

import com.makao.deploy.entity.base.Auditable
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long,
    private var name: String,
    private var description: String,
    private var gitRepositoryUrl: String
) : Auditable()