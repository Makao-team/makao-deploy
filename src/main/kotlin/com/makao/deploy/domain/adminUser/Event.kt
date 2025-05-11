package com.makao.deploy.domain.adminUser

data class UserSignedUpEvent(
    val userId: Long,
    val email: String
)