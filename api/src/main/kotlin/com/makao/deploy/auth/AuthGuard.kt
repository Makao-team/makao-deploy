package com.makao.deploy.auth

import com.makao.deploy.entity.AdminUserRole


@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthGuard(
    val roles: Array<AdminUserRole> = []
) 