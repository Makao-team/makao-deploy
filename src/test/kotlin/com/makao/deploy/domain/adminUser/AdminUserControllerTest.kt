package com.makao.deploy.domain.adminUser

import com.makao.deploy.config.IntegrationTest
import com.makao.deploy.config.PostgreInitializer
import io.kotest.common.runBlocking
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.springframework.test.context.ContextConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
            val response = client.post(getUrl("/admin-user/sign-up/request")) {
                contentType(ContentType.Application.Json)
                setBody(
                    AdminUserDTO.RequestSignUpRequest(
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
            val response = client.post(getUrl("/admin-user/sign-up/request")) {
                contentType(ContentType.Application.Json)
                setBody(
                    AdminUserDTO.RequestSignUpRequest(
                        email = "test@email.com",
                        password = "securePassword123!",
                        name = "makao",
                    )
                )
            }

            assertTrue(response.bodyAsText().contains("이메일이 중복되었어요."))
        }

        @Test
        fun `회원가입 실패 - 비밀번호 형식 오류`() = runBlocking {
            val response = client.post(getUrl("/admin-user/sign-up/request")) {
                contentType(ContentType.Application.Json)
                setBody(
                    AdminUserDTO.RequestSignUpRequest(
                        email = "test@email.com",
                        password = "short",
                        name = "makao",
                    )
                )
            }

            assertTrue(response.bodyAsText().contains("비밀번호는 8자 이상이어야해요."))
        }
    }

    fun insertUser(email: String, password: String = "hashed_password", name: String = "test user") {
        transactionTemplate.executeWithoutResult {
            val query = """
            INSERT INTO admin_user (email, password, name)
            VALUES (?, ?, ?)
        """.trimIndent()

            em.createNativeQuery(query)
                .setParameter(1, email)
                .setParameter(2, password)
                .setParameter(3, name)
                .executeUpdate()
        }
    }
}