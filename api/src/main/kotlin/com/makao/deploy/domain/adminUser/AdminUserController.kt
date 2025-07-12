package com.makao.deploy.domain.adminUser

import com.makao.deploy.auth.AuthGuard
import com.makao.deploy.entity.AdminUserRole
import com.makao.deploy.response.CommonResponse
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/admin-user")
class AdminUserController(
    private val adminUserService: AdminUserService,
) {
    @PostMapping("/sign-up/request")
    fun requestSignUp(@Valid @RequestBody dto: RequestSignUpRequest): ResponseEntity<CommonResponse<Long>> {
        return CommonResponse.success(adminUserService.requestSignUp(dto))
    }

    @AuthGuard(roles = [AdminUserRole.SUPER_ADMIN])
    @PostMapping("/sign-up/confirm")
    fun confirmSignUp(@Valid @RequestBody dto: ConfirmSignUpRequest): ResponseEntity<CommonResponse<Long>> {
        return CommonResponse.success(adminUserService.confirmSignUp(dto))
    }

    @PostMapping("/sign-in")
    fun signIn(
        @Valid @RequestBody dto: SignInRequest,
        session: HttpSession
    ): ResponseEntity<CommonResponse<Long>> {
        val adminUser = adminUserService.signIn(dto)
        session.setAttribute("userId", adminUser.id!!)
        session.setAttribute("role", adminUser.role.value)

        return CommonResponse.success(adminUser.id!!)
    }

    @PostMapping("/logout")
    fun logout(session: HttpSession): ResponseEntity<CommonResponse<String>> {
        session.invalidate()
        return CommonResponse.success("로그아웃 되었습니다.")
    }

    @GetMapping("/check")
    fun check(session: HttpSession): ResponseEntity<CommonResponse<Boolean>> {
        val userId = session.getAttribute("userId")
        val role = session.getAttribute("role")

        return CommonResponse.success(
            adminUserService.check(
                userId as Long?,
                role as String?
            )
        )
    }

    // [TODO] 세션 주입 mock 만들고 지우기
    @PostMapping("/test-login")
    fun testLogin(
        @RequestParam userId: Long,
        @RequestParam role: AdminUserRole,
        session: HttpSession
    ): ResponseEntity<CommonResponse<String>> {
        session.setAttribute("userId", userId)
        session.setAttribute("role", role)
        return CommonResponse.success("테스트 로그인 성공")
    }
}