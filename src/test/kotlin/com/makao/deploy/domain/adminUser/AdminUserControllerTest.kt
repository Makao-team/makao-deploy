package com.makao.deploy.domain.adminUser

import com.makao.deploy.config.IntegrationTest
import com.makao.deploy.config.PostgreInitializer
import io.kotest.common.runBlocking
import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.springframework.test.context.ContextConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals

@ContextConfiguration(initializers = [PostgreInitializer::class])
class AdminUserControllerTest : IntegrationTest() {
    @AfterEach
    fun tearDown() {
        transactionTemplate.executeWithoutResult {
            em.createNativeQuery("TRUNCATE TABLE admin_user").executeUpdate()
        }
    }

    @Nested
    inner class `회원 가입` {
        @Test
        fun `회원가입 성공`() = runBlocking {
            val response = client.post(getUrl("/admin-user/sign-up")) {
                contentType(ContentType.Application.Json)
                setBody(
                    AdminUserDTO.SignUpRequest(
                        email = "test@example.com",
                        password = "securePassword123!",
                        name = "makao",
                    )
                )
            }

            assertEquals(HttpStatusCode.OK, response.status)
        }

        @Test
        fun `회원가입 실패 - 이메일 중복`() = runBlocking {
            insertUser("test@email.com")
            val response = client.post(getUrl("/admin-user/sign-up")) {
                contentType(ContentType.Application.Json)
                setBody(
                    AdminUserDTO.SignUpRequest(
                        email = "test@email.com",
                        password = "securePassword123!",
                        name = "makao",
                    )
                )
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }

    fun insertUser(email: String) {
        transactionTemplate.executeWithoutResult {
            em.createNativeQuery(
                """
            INSERT INTO admin_user (email, password, name)
            VALUES ('$email', 'hashed_password', 'test user')
            """.trimIndent()
            ).executeUpdate()
        }
    }
}