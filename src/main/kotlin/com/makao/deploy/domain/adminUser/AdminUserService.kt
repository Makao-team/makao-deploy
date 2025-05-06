package com.makao.deploy.domain.adminUser

import com.makao.deploy.client.SlackChannelName
import com.makao.deploy.client.SlackClient
import com.makao.deploy.repository.AdminUserRepository
import com.makao.deploy.response.BadRequestException
import com.makao.deploy.util.StringEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminUserService(
    private val adminUserRepository: AdminUserRepository,
    private val slackClient: SlackClient
) {
    @Transactional
    fun signUp(dto: AdminUserDTO.SignUpRequest): Long {
        if (adminUserRepository.existsByEmail(dto.email))
            throw BadRequestException("이메일이 중복되었어요.")

        val password = StringEncoder.encode(dto.password)

        slackClient.sendMessage(
            SlackChannelName.NOTIFICATION, """
            ${dto.name}(${dto.email})님이 관리자 계정으로 가입하셨어요.
            승인이 필요해요.
            """.trimIndent()
        )
        return adminUserRepository.save(dto.toEntity(password)).id!!
    }
}
