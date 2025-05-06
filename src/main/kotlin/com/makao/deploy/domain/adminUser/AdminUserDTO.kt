package com.makao.deploy.domain.adminUser

import com.makao.deploy.entity.AdminUser
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

class AdminUserDTO {
    data class SignUpRequest(
        @Email(message = "이메일 형식이 아니에요.")
        @NotBlank(message = "이메일을 입력해주세요.")
        val email: String,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        val password: String,

        @NotBlank(message = "이름을 입력해주세요.")
        val name: String,
    ) {
        fun toEntity(password: String): AdminUser {
            return AdminUser(name = name, email = email, password = password)
        }
    }

    data class LoginRequest(
        val email: String,
        val password: String,
    )
}