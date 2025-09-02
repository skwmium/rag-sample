package com.skwmium.ragsample

import org.springframework.ai.chat.client.ChatClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class RagSampleApplication {
    @Bean
    fun chatClient(chatClientBuilder: ChatClient.Builder): ChatClient {
        return chatClientBuilder.build()
    }
}

fun main(args: Array<String>) {
    runApplication<RagSampleApplication>(*args)
}
