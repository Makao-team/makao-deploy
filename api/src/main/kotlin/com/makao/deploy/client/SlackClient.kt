package com.makao.deploy.client

import com.makao.deploy.config.ClientProperties
import com.makao.deploy.response.InternalServerException
import com.slack.api.Slack
import com.slack.api.webhook.Payload

interface SlackClient {
    fun sendMessage(channel: SlackChannelName, message: String)
}

enum class SlackChannelName {
    NOTIFICATION,
}

class SlackClientImpl(private val clientProperties: ClientProperties) : SlackClient {
    private val slack = Slack.getInstance()
    private val log = org.slf4j.LoggerFactory.getLogger(SlackClientImpl::class.java)

    override fun sendMessage(channel: SlackChannelName, message: String) {
        val webhookUrl = when (channel) {
            SlackChannelName.NOTIFICATION -> clientProperties.slack.webhookUrl
        }

        runCatching { slack.send(webhookUrl, Payload.builder().text(message).build()) }
            .onFailure { throw InternalServerException("슬랙 메시지 전송 실패: ${it.message}", it) }
            .onSuccess { log.info("슬랙 메시지 전송 완료: $channel: $message") }
    }
}

class NoOpSlackClientImpl : SlackClient {
    override fun sendMessage(channel: SlackChannelName, message: String) {
        // No operation
    }
}