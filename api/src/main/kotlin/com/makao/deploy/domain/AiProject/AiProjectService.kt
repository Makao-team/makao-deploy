package com.makao.deploy.domain.AiProject

import com.makao.deploy.entity.AiProjectRepository
import com.makao.deploy.response.BadRequestException
import com.makao.deploy.response.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AiProjectService(
    private val aiProjectRepository: AiProjectRepository
) {
    @Transactional
    fun init(dto: SaveRequest, userId: Long): Long {
        return aiProjectRepository.save(dto.toEntity(userId)).id
            ?: throw BadRequestException("AI 프로젝트 생성에 실패했습니다.")
    }

    fun findAll(userId: Long): List<ProjectResponse> {
        return aiProjectRepository.findByCreatedBy(userId)
            .map { ProjectResponse.from(it) }
    }

    fun findById(id: Long, userId: Long): ProjectResponse {
        val project = aiProjectRepository.findByIdOrNull(id)
            ?: throw NotFoundException("프로젝트를 찾을 수 없습니다.")

        if (project.createdBy != userId) {
            throw BadRequestException("프로젝트에 접근할 권한이 없습니다.")
        }

        return ProjectResponse.from(project)
    }

    @Transactional
    fun update(id: Long, dto: UpdateRequest, userId: Long): Long {
        val project = aiProjectRepository.findByIdOrNull(id)
            ?: throw NotFoundException("프로젝트를 찾을 수 없습니다.")

        if (project.createdBy != userId) {
            throw BadRequestException("프로젝트에 접근할 권한이 없습니다.")
        }

        project.name = dto.name
        project.description = dto.description
        project.status = dto.status

        return aiProjectRepository.save(project).id
            ?: throw BadRequestException("프로젝트 수정에 실패했습니다.")
    }

    @Transactional
    fun delete(id: Long, userId: Long): Long {
        val project = aiProjectRepository.findByIdOrNull(id)
            ?: throw NotFoundException("프로젝트를 찾을 수 없습니다.")

        if (project.createdBy != userId) {
            throw BadRequestException("프로젝트에 접근할 권한이 없습니다.")
        }

        project.isArchived = true

        return aiProjectRepository.save(project).id
            ?: throw BadRequestException("프로젝트 삭제에 실패했습니다.")
    }
}
