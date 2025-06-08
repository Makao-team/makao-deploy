package com.makao.deploy.domain.adminUser

import com.makao.deploy.response.CommonResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/admin-user")
class AdminUserController(
    private val adminUserService: AdminUserService,
) {
    @PostMapping("/sign-up/request")
    fun requestSignUp(@Valid @RequestBody dto: AdminUserDTO.RequestSignUpRequest): ResponseEntity<CommonResponse<Long>> {
        return CommonResponse.success(adminUserService.requestSignUp(dto))
    }

    // TODO: AuthGuard(SuperAdmin) required
    @PostMapping("/sign-up/confirm")
    fun confirmSignUp(@Valid @RequestBody dto: AdminUserDTO.ConfirmSignUpRequest): ResponseEntity<CommonResponse<Long>> {
        return CommonResponse.success(adminUserService.confirmSignUp(dto))
    }

    @PostMapping("/sign-in")
    fun signIn(@Valid @RequestBody dto: AdminUserDTO.SignInRequest): ResponseEntity<CommonResponse<Long>> {
        return CommonResponse.success(adminUserService.signIn(dto))
    }
}