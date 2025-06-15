package com.makao.deploy.auth

import com.makao.deploy.entity.AdminUserRole
import com.makao.deploy.response.UnauthorizedException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.context.annotation.Profile

@Aspect
@Component
@Profile("!test")
class AuthGuardAspect {
    @Before("@annotation(com.makao.deploy.auth.AuthGuard)")
    fun checkAuth(joinPoint: JoinPoint) {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val annotation = method.getAnnotation(AuthGuard::class.java)
        val allowedRoles = annotation.roles

        val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        val session = requestAttributes.request.session
        val roleObj = session.getAttribute("role")
        val role = when (roleObj) {
            is AdminUserRole -> roleObj
            is String -> try {
                AdminUserRole.valueOf(roleObj)
            } catch (e: Exception) {
                null
            }

            else -> null
        }

        if (role == null || allowedRoles.none { it == role })
            throw UnauthorizedException(message = "권한이 없어요.")
    }
}