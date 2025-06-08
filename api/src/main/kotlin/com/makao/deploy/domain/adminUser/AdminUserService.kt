package com.makao.deploy.domain.adminUser

import com.makao.deploy.client.SlackChannelName
import com.makao.deploy.client.SlackClient
import com.makao.deploy.entity.AdminUser
import com.makao.deploy.entity.AdminUserRole
import com.makao.deploy.repository.AdminUserRepository
import com.makao.deploy.response.BadRequestException
import com.makao.deploy.util.StringEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminUserService(
    private val adminUserRepository: AdminUserRepository,
    private val slackClient: SlackClient,
) {
    @Transactional
    fun requestSignUp(dto: AdminUserDTO.RequestSignUpRequest): Long {
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

    @Transactional
    fun confirmSignUp(dto: AdminUserDTO.ConfirmSignUpRequest): Long {
        val adminUser = adminUserRepository.findByEmail(dto.email)
            ?: throw BadRequestException("가입 요청이 존재하지 않아요.")

        if (adminUser.isConfirmed)
            throw BadRequestException("이미 가입이 완료된 계정이에요.")

        if (adminUser.role != AdminUserRole.ADMIN)
            throw BadRequestException("일반 관리자가 아닌 관리자가 가입 요청을 했어요. 슈퍼 관리자에게 문의해주세요.")

        adminUser.isConfirmed = true
        return adminUserRepository.save(adminUser).id!!
    }

    @Transactional(readOnly = true)
    fun signIn(dto: AdminUserDTO.SignInRequest): AdminUser {
        val adminUser = adminUserRepository.findByEmail(dto.email)
            ?: throw BadRequestException("가입된 계정이 아니에요.")

        if (!adminUser.isConfirmed)
            throw BadRequestException("가입이 승인되지 않은 계정이에요. 슈퍼 관리자에게 문의해주세요.")

        if (!StringEncoder.match(dto.password, adminUser.password))
            throw BadRequestException("비밀번호가 일치하지 않아요.")

        return adminUser
    }

    fun check(userId: Long?, role: String?): Boolean {
        if (userId == null || role == null)
            throw BadRequestException("로그인 정보가 없어요.")

        return true
    }
}
