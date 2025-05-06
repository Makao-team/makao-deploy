package com.makao.deploy.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "client")
data class ClientProperties @ConstructorBinding constructor(
    val slack: SlackProperties
) {
    data class SlackProperties(
        val webhookUrl: String
    ) {
        fun isValid(): Boolean {
            return webhookUrl.isNotBlank() && !webhookUrl.contains("\${")
        }
    }
}