package com.makao.deploy.config

import io.kotest.core.spec.style.StringSpec
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.transaction.support.TransactionTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class IntegrationTest : StringSpec() {
    @LocalServerPort
    protected var port: Int = 0

    @Autowired
    protected lateinit var em: EntityManager

    @Autowired
    protected lateinit var transactionTemplate: TransactionTemplate

    protected val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }
    }

    protected fun getUrl(path: String): String {
        return "http://localhost:$port$path"
    }
}