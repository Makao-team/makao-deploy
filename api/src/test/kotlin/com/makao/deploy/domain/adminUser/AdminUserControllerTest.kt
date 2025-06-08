package com.makao.deploy.domain.adminUser

import at.favre.lib.crypto.bcrypt.BCrypt
import com.makao.deploy.config.IntegrationTest
import com.makao.deploy.config.PostgreInitializer
import com.makao.deploy.entity.AdminUserRole
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

            assertEquals(HttpStatusCode.BadRequest, response.status)
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

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }

    @Nested
    inner class `회원 가입 승인` {
        @Test
        fun `회원가입 승인 성공`() = runBlocking {
            val userId = insertUser("test@email.com")
            val loginResponse = client.post(getUrl("/admin-user/test-login")) {
                parameter("userId", userId)
                parameter("role", AdminUserRole.SUPER_ADMIN)
            }
            val cookies = loginResponse.setCookie()

            val response = client.post(getUrl("/admin-user/sign-up/confirm")) {
                contentType(ContentType.Application.Json)
                setBody(
                    AdminUserDTO.ConfirmSignUpRequest(email = "test@email.com")
                )
                cookies.forEach { cookie ->
                    header("Cookie", "${cookie.name}=${cookie.value}")
                }
            }

            assertEquals(HttpStatusCode.OK, response.status)
        }

        @Test
        fun `회원가입 승인 실패 - 가입 요청이 존재하지 않음`() = runBlocking {
            val loginResponse = client.post(getUrl("/admin-user/test-login")) {
                parameter("userId", 1L)
                parameter("role", AdminUserRole.SUPER_ADMIN)
            }
            val cookies = loginResponse.setCookie()

            val response = client.post(getUrl("/admin-user/sign-up/confirm")) {
                contentType(ContentType.Application.Json)
                setBody(
                    AdminUserDTO.ConfirmSignUpRequest(email = "test@email.com")
                )
                cookies.forEach { cookie ->
                    header("Cookie", "${cookie.name}=${cookie.value}")
                }
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }

        @Test
        fun `회원가입 승인 실패 - 이미 가입된 계정`() = runBlocking {
            val userId = insertUser("test@email.com", isConfirmed = true)
            val loginResponse = client.post(getUrl("/admin-user/test-login")) {
                parameter("userId", userId)
                parameter("role", AdminUserRole.SUPER_ADMIN)
            }
            val cookies = loginResponse.setCookie()

            val response = client.post(getUrl("/admin-user/sign-up/confirm")) {
                contentType(ContentType.Application.Json)
                setBody(
                    AdminUserDTO.ConfirmSignUpRequest(email = "test@email.com")
                )
                cookies.forEach { cookie ->
                    header("Cookie", "${cookie.name}=${cookie.value}")
                }
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }

        @Test
        fun `회원가입 승인 실패 - 일반 관리자가 아닌 관리자 계정`() = runBlocking {
            val userId = insertUser("test@email.com", role = AdminUserRole.SUPER_ADMIN)
            val loginResponse = client.post(getUrl("/admin-user/test-login")) {
                parameter("userId", userId)
                parameter("role", AdminUserRole.SUPER_ADMIN)
            }
            val cookies = loginResponse.setCookie()

            val response = client.post(getUrl("/admin-user/sign-up/confirm")) {
                contentType(ContentType.Application.Json)
                setBody(
                    AdminUserDTO.ConfirmSignUpRequest(email = "test@email.com")
                )
                cookies.forEach { cookie ->
                    header("Cookie", "${cookie.name}=${cookie.value}")
                }
            }

            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }

    @Nested
    inner class `로그인` {
        @Test
        fun `로그인 성공`() = runBlocking {
            insertUser("test@email.com", password = "password123!", isConfirmed = true)
            val response = client.post(getUrl("/admin-user/sign-in")) {
                contentType(ContentType.Application.Json)
                setBody(
                    AdminUserDTO.SignInRequest(
                        email = "test@email.com",
                        password = "password123!"
                    )
                )
            }
            assertEquals(HttpStatusCode.OK, response.status)
        }

        @Test
        fun `로그인 실패 - 가입되지 않은 계정`() = runBlocking {
            val response = client.post(getUrl("/admin-user/sign-in")) {
                contentType(ContentType.Application.Json)
                setBody(
                    AdminUserDTO.SignInRequest(
                        email = "test@email.com",
                        password = "password123!"
                    )
                )
            }
            assertEquals(HttpStatusCode.BadRequest, response.status)
        }

        @Test
        fun `로그인 실패 - 가입 승인되지 않은 계정`() = runBlocking {
            insertUser("test@email.com", isConfirmed = false)
            val response = client.post(getUrl("/admin-user/sign-in")) {
                contentType(ContentType.Application.Json)
                setBody(
                    AdminUserDTO.SignInRequest(
                        email = "test@email.com",
                        password = "password123!"
                    )
                )
            }
            assertEquals(HttpStatusCode.BadRequest, response.status)
        }

        @Test
        fun `로그인 실패 - 비밀번호 불일치`() = runBlocking {
            insertUser("test@email.com", isConfirmed = true, password = "correctPassword123!")
            val response = client.post(getUrl("/admin-user/sign-in")) {
                contentType(ContentType.Application.Json)
                setBody(
                    AdminUserDTO.SignInRequest(
                        email = "test@email.com",
                        password = "wrongPassword123!"
                    )
                )
            }
            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }

    fun insertUser(
        email: String,
        password: String = "password123!",
        name: String = "test user",
        role: AdminUserRole = AdminUserRole.ADMIN,
        isConfirmed: Boolean = false
    ): Long? {
        val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        var userId: Long? = null
        transactionTemplate.executeWithoutResult {
            val query = """
            INSERT INTO admin_user (email, password, name, role, is_confirmed)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id
        """.trimIndent()

            val result = em.createNativeQuery(query)
                .setParameter(1, email)
                .setParameter(2, hashedPassword)
                .setParameter(3, name)
                .setParameter(4, role)
                .setParameter(5, isConfirmed)
                .singleResult
            userId = when (result) {
                is Number -> result.toLong()
                else -> (result as? java.math.BigInteger)?.toLong() ?: null
            }
        }
        return userId
    }
}