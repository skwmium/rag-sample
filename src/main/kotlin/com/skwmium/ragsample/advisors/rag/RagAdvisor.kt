package com.skwmium.ragsample.advisors.rag

import com.skwmium.ragsample.advisors.ExpansionQueryAdvisor
import org.springframework.ai.chat.client.ChatClientRequest
import org.springframework.ai.chat.client.ChatClientResponse
import org.springframework.ai.chat.client.advisor.api.AdvisorChain
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore

class RagAdvisor(
    private val vectorStore: VectorStore,
    private val rerankEngine: BM25RerankEngine,
    private val order: Int,
) : BaseAdvisor {

    private val promptTemplate = PromptTemplate.builder().template(
        """
            CONTEXT: {context}
            Question: {question}
        """.trimIndent()
    ).build()

    private val searchRequest = SearchRequest
        .builder()
        .topK(4)
        .similarityThreshold(0.62)
        .build()

    override fun before(
        chatClientRequest: ChatClientRequest,
        advisorChain: AdvisorChain
    ): ChatClientRequest {
        val originalUserQuestion = chatClientRequest.prompt().userMessage.text
        val queryToRag = chatClientRequest
            .context()
            .getOrDefault(ExpansionQueryAdvisor.ENRICHED_QUESTION, originalUserQuestion)
            .toString()

        val documents = vectorStore.similaritySearch(
            SearchRequest.from(searchRequest)
                .query(queryToRag)
                .topK(searchRequest.topK * 2)
                .build()
        )

        if (documents.isNullOrEmpty()) {
            return chatClientRequest
                .mutate()
                .context("CONTEXT", "ТУТ ПУСТО - ни один документ не обнаружен")
                .build()
        }

        val rerankedDocuments = rerankEngine.rerank(documents, queryToRag, searchRequest.topK)

        val llmContext = rerankedDocuments
            .mapNotNull { it.text }
            .joinToString(System.lineSeparator())

        val finalUserPrompt = promptTemplate.render(
            mapOf(
                "context" to llmContext,
                "question" to originalUserQuestion,
            )
        )

        return chatClientRequest
            .mutate()
            .prompt(
                chatClientRequest
                    .prompt()
                    .augmentUserMessage(finalUserPrompt)
            ).build()
    }

    override fun after(
        chatClientResponse: ChatClientResponse,
        advisorChain: AdvisorChain
    ): ChatClientResponse {
        return chatClientResponse
    }

    override fun getOrder(): Int {
        return order
    }
}