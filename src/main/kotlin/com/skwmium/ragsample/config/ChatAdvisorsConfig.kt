package com.skwmium.ragsample.config

import com.skwmium.ragsample.advisors.ExpansionQueryAdvisor
import com.skwmium.ragsample.advisors.rag.BM25RerankEngine
import com.skwmium.ragsample.advisors.rag.RagAdvisor
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.client.advisor.api.Advisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatAdvisorsConfig {

    @Bean
    @ConditionalOnBooleanProperty(prefix = "app.advisors.expansion", name = ["enabled"])
    fun expansionQueryAdvisor(@Qualifier("expansionChatClient") expansionChatClient: ChatClient): Advisor {
        return ExpansionQueryAdvisor(
            chatClient = expansionChatClient,
            order = 0,
        )
    }

    @Bean
    @ConditionalOnBooleanProperty(prefix = "app.advisors.history", name = ["enabled"])
    fun historyAdvisor(chatMemory: ChatMemory): Advisor {
        return MessageChatMemoryAdvisor
            .builder(chatMemory)
            .order(1)
            .build()
    }

    @Bean
    @ConditionalOnBooleanProperty(prefix = "app.advisors.history", name = ["enabled"])
    fun loggingAdvisorAfterHistory(): Advisor {
        return SimpleLoggerAdvisor
            .builder()
            .order(2)
            .build()
    }

    @Bean
    @ConditionalOnBooleanProperty(prefix = "app.advisors.rag", name = ["enabled"])
    fun ragAdvisor(vectorStore: VectorStore, rerankEngine: BM25RerankEngine): Advisor {
        return RagAdvisor(
            vectorStore = vectorStore,
            rerankEngine = rerankEngine,
            order = 3,
        )
    }

    @Bean
    @ConditionalOnBooleanProperty(prefix = "app.advisors.rag", name = ["enabled"])
    fun loggingAdvisorAfterRag(): Advisor {
        return SimpleLoggerAdvisor
            .builder()
            .order(4)
            .build()
    }
}