package com.makao.deploy.config

import org.slf4j.LoggerFactory
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import java.io.Closeable

class PostgreInitializer : ApplicationContextInitializer<ConfigurableApplicationContext>, Closeable {
    companion object {
        private val logger = LoggerFactory.getLogger(PostgreInitializer::class.java)
        private const val POSTGRES_IMAGE = "postgres:15"
        private var container: PostgreSQLContainer<*>? = null
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        logger.info("Initializing Postgres test container")

        if (container == null) {
            container = PostgreSQLContainer(POSTGRES_IMAGE)
                .withDatabaseName("makao-deploy-test")
                .withUsername("test")
                .withPassword("test")
                .withUrlParam("stringtype", "unspecified")
                .apply { start() }
        }

        TestPropertyValues.of(
            "spring.datasource.url=" + container!!.jdbcUrl,
            "spring.datasource.username=" + container!!.username,
            "spring.datasource.password=" + container!!.password,
            "spring.datasource.driver-class-name=org.postgresql.Driver",
            "spring.jpa.hibernate.ddl-auto=none",
            "spring.sql.init.mode=always",
            "spring.sql.init.schema-locations=classpath:db/schema.sql",
            "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
        ).applyTo(applicationContext.environment)
    }

    override fun close() {
        if (container != null) {
            container.apply { close() }
            container = null
        }
    }
}