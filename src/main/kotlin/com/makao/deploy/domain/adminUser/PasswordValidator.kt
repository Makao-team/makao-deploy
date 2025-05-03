package com.makao.deploy.domain.adminUser

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext


class PasswordValidator : ConstraintValidator<Password, String> {
    override fun isValid(password: String, context: ConstraintValidatorContext): Boolean {
        if (password.isBlank()) {
            return addConstraintViolation(context, "비밀번호를 입력해주세요.")
        }

        if (password.length < 8) {
            return addConstraintViolation(context, "비밀번호는 8자 이상이어야해요.")
        }

        if (!password.matches(".*[A-Za-z].*".toRegex())) {
            return addConstraintViolation(context, "비밀번호는 영문자를 포함해야해요.")
        }

        if (!password.matches(".*\\d.*".toRegex())) {
            return addConstraintViolation(context, "비밀번호는 숫자를 포함해야해요.")
        }

        if (!password.matches(".*[@$!%*?&].*".toRegex())) {
            return addConstraintViolation(context, "비밀번호는 특수문자를 포함해야해요.")
        }

        return true
    }

    private fun addConstraintViolation(context: ConstraintValidatorContext, message: String): Boolean {
        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation()
        return false
    }
}