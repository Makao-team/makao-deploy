package com.makao.deploy.domain.adminUser

import com.makao.deploy.client.SlackChannelName
import com.makao.deploy.client.SlackClient
import com.makao.deploy.entity.AdminUser
import com.makao.deploy.entity.AdminUserRepository
import com.makao.deploy.entity.AdminUserRole
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
    fun requestSignUp(dto: RequestSignUpRequest): Long {
        if (adminUserRepository.existsByEmail(dto.email))
            throw BadRequestException("이메일이 중복되었습니다.")

        val password = StringEncoder.encode(dto.password)

        slackClient.sendMessage(
            SlackChannelName.NOTIFICATION, """
            ${dto.name}(${dto.email})님이 관리자 계정으로 가입했습니다.
            승인이 필요합니다.
            """.trimIndent()
        )
        return adminUserRepository.save(dto.toEntity(password)).id!!
    }

    @Transactional
    fun confirmSignUp(dto: ConfirmSignUpRequest): Long {
        val adminUser = adminUserRepository.findByEmail(dto.email)
            ?: throw BadRequestException("가입 요청이 존재하지 않습니다.")

        if (adminUser.isConfirmed)
            throw BadRequestException("이미 가입이 완료된 계정입니다.")

        if (adminUser.role != AdminUserRole.ADMIN)
            throw BadRequestException("일반 관리자가 아닌 관리자가 가입 요청을 했습니다. 슈퍼 관리자에게 문의해주세요.")

        adminUser.isConfirmed = true
        return adminUserRepository.save(adminUser).id!!
    }

    @Transactional(readOnly = true)
    fun signIn(dto: SignInRequest): AdminUser {
        val adminUser = adminUserRepository.findByEmail(dto.email)
            ?: throw BadRequestException("가입된 계정이 아닙니다.")

        if (!adminUser.isConfirmed)
            throw BadRequestException("가입이 승인되지 않은 계정입니다. 슈퍼 관리자에게 문의해주세요.")

        if (!StringEncoder.match(dto.password, adminUser.password))
            throw BadRequestException("비밀번호가 일치하지 않습니다.")

        return adminUser
    }

    fun check(userId: Long?, role: String?): Boolean {
        if (userId == null || role == null)
            throw BadRequestException("로그인 정보가 없습니다.")

        return true
    }
}
