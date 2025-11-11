package com.skwmium.ragsample.config

import com.skwmium.ragsample.memory.PostgresChatMemory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.api.Advisor
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatClientConfiguration(
    private val postgresChatMemory: PostgresChatMemory,
    private val vectorStore: VectorStore,
) {

    @Bean
    fun chatClient(
        chatClientBuilder: ChatClient.Builder,
        historyAdvisor: Advisor,
        ragAdvisor: Advisor,
    ): ChatClient {
        return chatClientBuilder
            .defaultAdvisors(historyAdvisor, ragAdvisor)
            .build()
    }

    @Bean
    fun historyAdvisor(): Advisor {
        return MessageChatMemoryAdvisor
            .builder(postgresChatMemory)
            .build()
    }

    @Bean
    fun ragAdvisor(): Advisor {
        val searchRequest = SearchRequest
            .builder()
            .topK(4)
            .build()

        val promptTemplate = PromptTemplate(
            "{query}\n\n" +
                    "Контекст:\n" +
                    "---------------------\n" +
                    "{question_answer_context}\n" +
                    "---------------------\n\n" +
                    "Отвечай только на основе контекста выше. Если информации нет в контексте, сообщи, что не можешь ответить."
        )

        return QuestionAnswerAdvisor
            .builder(vectorStore)
            .searchRequest(searchRequest)
            .promptTemplate(promptTemplate)
            .build()
    }
}