package com.makao.deploy.config

import com.makao.deploy.client.NoOpSlackClientImpl
import com.makao.deploy.client.SlackClient
import com.makao.deploy.client.SlackClientImpl
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ClientConfig {
    private val log = LoggerFactory.getLogger(ClientConfig::class.java)

    @Bean
    fun slackClient(clientProperties: ClientProperties): SlackClient {
        return if (!clientProperties.slack.isValid()) {
            log.info("슬랙 설정이 올바르지 않아요. NoOpSlackClientImpl을 사용해요.")
            NoOpSlackClientImpl()
        } else {
            SlackClientImpl(clientProperties)
        }
    }
}