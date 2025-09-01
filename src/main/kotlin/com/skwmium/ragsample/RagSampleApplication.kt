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
    val context = runApplication<RagSampleApplication>(*args)
    val chatClient = context.getBean(ChatClient::class.java)
    val response = chatClient.prompt()
        .user("Дай первую строчку богемской рапсодии")
        .call()
        .content()
    println(response)
}
