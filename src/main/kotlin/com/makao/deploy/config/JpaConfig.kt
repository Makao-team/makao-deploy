package com.makao.deploy.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan("com.makao.deploy.entity")
@EnableJpaAuditing
@EnableJpaRepositories("com.makao.deploy.repository")
class JpaConfig