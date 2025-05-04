package com.makao.deploy.client

import com.makao.deploy.config.ClientProperties
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

    override fun sendMessage(channel: SlackChannelName, message: String) {
        var webhookUrl: String
        when (channel) {
            SlackChannelName.NOTIFICATION -> webhookUrl = clientProperties.slack.webhookUrl
        }

        slack.send(webhookUrl, Payload.builder().text(message).build())
    }
}

class NoOpSlackClientImpl : SlackClient {
    override fun sendMessage(channel: SlackChannelName, message: String) {
        // No operation
    }
}