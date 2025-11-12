package com.skwmium.ragsample.config

import com.skwmium.ragsample.memory.PostgresChatMemory
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.client.advisor.api.Advisor
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatAdvisorsConfig(
    private val postgresChatMemory: PostgresChatMemory,
    private val vectorStore: VectorStore,
) {

    @Bean
    fun loggingAdvisor(): Advisor {
        return SimpleLoggerAdvisor
            .builder()
            .build()
    }

    @Bean
    @ConditionalOnBooleanProperty(prefix = "app.advisors.history", name = ["enabled"])
    fun historyAdvisor(): Advisor {
        return MessageChatMemoryAdvisor
            .builder(postgresChatMemory)
            .build()
    }

    @Bean
    @ConditionalOnBooleanProperty(prefix = "app.advisors.rag", name = ["enabled"])
    fun ragAdvisor(): Advisor {
        val searchRequest = SearchRequest
            .builder()
            .topK(4)
            .similarityThreshold(0.65)
            .build()

        val promptTemplate = PromptTemplate(RAG_PROMPT_TEMPLATE.trimIndent())

        return QuestionAnswerAdvisor
            .builder(vectorStore)
            .searchRequest(searchRequest)
            .promptTemplate(promptTemplate)
            .build()
    }

    private companion object {
        private const val RAG_PROMPT_TEMPLATE = """
                {query}
                
                Контекст:
                ---------------------
                {question_answer_context}
                ---------------------
                
                Отвечай только на основе контекста выше.
                Если информации нет в контексте, сообщи, что не можешь ответить.
            """
    }
}