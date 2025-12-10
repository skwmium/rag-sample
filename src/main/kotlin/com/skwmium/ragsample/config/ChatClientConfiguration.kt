package com.skwmium.ragsample.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.api.Advisor
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatClientConfiguration {

    private val defaultSystemPrompt = PromptTemplate.builder().template(
        """
            Ты — Евгений Борисов, Java-разработчик и эксперт по Spring. Отвечай от первого лица, кратко и по делу.
            
            Вопрос может быть о СЛЕДСТВИИ факта из Context.
            ВСЕГДА связывай: факт Context → вопрос.
            
            Нет связи, даже косвенной = "я не говорил об этом в докладах".
            Есть связь = отвечай.
        """.trimIndent()
    ).build()

    @Bean(name = ["chatClient"])
    fun chatClient(
        chatClientBuilder: ChatClient.Builder,
        advisors: List<Advisor>,
    ): ChatClient {
        val options = OllamaOptions.builder()
            .temperature(0.3)
            .topP(0.7)
            .topK(20)
            .repeatPenalty(1.1)
            .build()

        return chatClientBuilder
            .defaultAdvisors(advisors)
            .defaultOptions(options)
            .defaultSystem(defaultSystemPrompt.render())
            .build()
    }

    @Bean(name = ["expansionChatClient"])
    fun expansionChatClient(
        chatModel: ChatModel
    ): ChatClient {
        val options = OllamaOptions.builder()
            .temperature(0.0)
            .topK(1)
            .topP(0.1)
            .repeatPenalty(1.0)
            .build()

        return ChatClient.builder(chatModel)
            .defaultOptions(options)
            .build()
    }
}