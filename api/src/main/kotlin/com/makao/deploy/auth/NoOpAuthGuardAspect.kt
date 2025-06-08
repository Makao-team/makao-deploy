package com.makao.deploy.auth

import org.aspectj.lang.annotation.Aspect
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Aspect
@Component
@Profile("test")
class NoOpAuthGuardAspect {
    @org.aspectj.lang.annotation.Before("@annotation(com.makao.deploy.auth.AuthGuard)")
    fun noOp() {
    }
} 