package com.makao.deploy.domain.AiProject

import com.makao.deploy.entity.AiProject
import com.makao.deploy.entity.AiProjectStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SaveRequest(
    @field:NotBlank(message = "프로젝트 이름을 입력해주세요.")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9-]+$",
        message = "프로젝트 이름은 영문, 숫자, -만 사용할 수 있어요."
    )
    val name: String,

    @field:NotBlank(message = "프로젝트 설명을 입력해주세요.")
    val description: String,
) {
    fun toEntity(userId: Long) = AiProject(
        name = name,
        description = description,
        createdBy = userId,
        status = AiProjectStatus.INACTIVE
    )
}

data class UpdateRequest(
    @field:NotBlank(message = "프로젝트 이름을 입력해주세요.")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9-]+$",
        message = "프로젝트 이름은 영문, 숫자, -만 사용할 수 있어요."
    )
    val name: String,

    @field:NotBlank(message = "프로젝트 설명을 입력해주세요.")
    val description: String,

    val status: AiProjectStatus
)

data class ProjectResponse(
    val id: Long,
    val name: String,
    val description: String,
    val status: AiProjectStatus,
    val createdBy: Long,
    val createdAt: String
) {
    companion object {
        fun from(project: AiProject): ProjectResponse {
            return ProjectResponse(
                id = project.id!!,
                name = project.name,
                description = project.description,
                status = project.status,
                createdBy = project.createdBy,
                createdAt = project.createdAt.toString()
            )
        }
    }
}
