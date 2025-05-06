package com.makao.deploy.domain.adminUser

import com.makao.deploy.response.CommonResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin-user")
class AdminUserController(
    private val adminUserService: AdminUserService,
) {
    @PostMapping("/sign-up")
    fun signUp(@Valid @RequestBody dto: AdminUserDTO.SignUpRequest): ResponseEntity<CommonResponse<Long>> {
        return CommonResponse.success(adminUserService.signUp(dto))
    }
}