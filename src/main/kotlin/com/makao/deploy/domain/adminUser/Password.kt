package com.makao.deploy.domain.adminUser

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Constraint(validatedBy = [PasswordValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Password(
    val message: String = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상이어야 합니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = []
)


