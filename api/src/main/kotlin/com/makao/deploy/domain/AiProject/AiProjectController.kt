package com.makao.deploy.domain.AiProject

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
@RequestMapping("/api/projects")
class AiProjectController(
    private val aiProjectService: AiProjectService
) {
    /** AI 프로젝트 생성 */
    @AuthGuard(roles = [AdminUserRole.ADMIN, AdminUserRole.SUPER_ADMIN])
    @PostMapping
    fun init(session: HttpSession, @Valid @RequestBody dto: SaveRequest): ResponseEntity<CommonResponse<Long>> {
        val userId = session.getAttribute("userId") as Long
        return CommonResponse.success(aiProjectService.init(dto, userId))
    }

    /** AI 프로젝트 목록 조회 */
    @AuthGuard(roles = [AdminUserRole.ADMIN, AdminUserRole.SUPER_ADMIN])
    @GetMapping
    fun findAll(session: HttpSession): ResponseEntity<CommonResponse<List<ProjectResponse>>> {
        val userId = session.getAttribute("userId") as Long
        return CommonResponse.success(aiProjectService.findAll(userId))
    }

    /** AI 프로젝트 상세 조회 */
    @AuthGuard(roles = [AdminUserRole.ADMIN, AdminUserRole.SUPER_ADMIN])
    @GetMapping("/{id}")
    fun getProject(
        session: HttpSession,
        @PathVariable id: Long
    ): ResponseEntity<CommonResponse<ProjectResponse>> {
        val userId = session.getAttribute("userId") as Long
        return CommonResponse.success(aiProjectService.findById(id, userId))
    }

    /** AI 프로젝트 수정 */
    @AuthGuard(roles = [AdminUserRole.ADMIN, AdminUserRole.SUPER_ADMIN])
    @PutMapping("/{id}")
    fun updateProject(
        session: HttpSession,
        @PathVariable id: Long,
        @Valid @RequestBody dto: UpdateRequest
    ): ResponseEntity<CommonResponse<Long>> {
        val userId = session.getAttribute("userId") as Long
        return CommonResponse.success(aiProjectService.update(id, dto, userId))
    }

    /** AI 프로젝트 삭제 (논리삭제) */
    @AuthGuard(roles = [AdminUserRole.ADMIN, AdminUserRole.SUPER_ADMIN])
    @DeleteMapping("/{id}")
    fun deleteProject(
        session: HttpSession,
        @PathVariable id: Long
    ): ResponseEntity<CommonResponse<Long>> {
        val userId = session.getAttribute("userId") as Long
        return CommonResponse.success(aiProjectService.delete(id, userId))
    }
}