package com.skwmium.ragsample.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.api.Advisor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatClientConfiguration {

    @Bean
    fun chatClient(
        chatClientBuilder: ChatClient.Builder,
        advisors: List<Advisor>,
    ): ChatClient {
        return chatClientBuilder
            .defaultAdvisors(advisors)
            .build()
    }
}