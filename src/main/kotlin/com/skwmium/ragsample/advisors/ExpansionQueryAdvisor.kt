package com.skwmium.ragsample.advisors

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.ChatClientRequest
import org.springframework.ai.chat.client.ChatClientResponse
import org.springframework.ai.chat.client.advisor.api.AdvisorChain
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor
import org.springframework.ai.chat.prompt.PromptTemplate

class ExpansionQueryAdvisor(
    private val chatClient: ChatClient,
    private val order: Int,
) : BaseAdvisor {

    private val promptTemplate = PromptTemplate.builder().template(
        """
                Instruction: Расширь поисковый запрос, добавив наиболее релевантные термины.
                
                СПЕЦИАЛИЗАЦИЯ ПО SPRING FRAMEWORK:
                - Жизненный цикл Spring бинов: конструктор → BeanPostProcessor → PostConstruct → прокси → ContextListener
                - Технологии: Dynamic Proxy, CGLib, reflection, аннотации, XML конфигурация
                - Компоненты: BeanFactory, ApplicationContext, BeanDefinition, MBean, JMX
                - Паттерны: dependency injection, AOP, профилирование, перехват методов

                ПРАВИЛА:
                1. Сохрани ВСЕ слова из исходного вопроса
                2. Добавь МАКСИМУМ ПЯТЬ наиболее важных термина
                3. Выбирай самые специфичные и релевантные слова
                4. Результат - простой список слов через пробел

                СТРАТЕГИЯ ВЫБОРА:
                - Приоритет: специализированные термины
                - Избегай общих слов
                - Фокусируйся на ключевых понятиях

                ПРИМЕРЫ:
                "что такое спринг" → "что такое спринг фреймворк Java"
                "как создать файл" → "как создать файл документ программа"

                Question: {question}
                Expanded query:
                
                """.trimIndent()
    ).build()

    override fun before(
        chatClientRequest: ChatClientRequest,
        advisorChain: AdvisorChain,
    ): ChatClientRequest {
        val originalUserQuestion = chatClientRequest.prompt().userMessage.text

        val prompt = promptTemplate.render(
            mapOf("question" to originalUserQuestion)
        )

        val enrichedQuestion = chatClient
            .prompt()
            .user(prompt)
            .call()
            .content()
            .orEmpty()

        val expansionRatio = enrichedQuestion.length / originalUserQuestion.length.toDouble()

        return chatClientRequest.mutate()
            .context(ORIGINAL_QUESTION, originalUserQuestion)
            .context(ENRICHED_QUESTION, enrichedQuestion)
            .context(EXPANSION_RATIO, expansionRatio)
            .build()
    }

    override fun after(
        chatClientResponse: ChatClientResponse,
        advisorChain: AdvisorChain,
    ): ChatClientResponse {
        return chatClientResponse
    }

    override fun getOrder(): Int {
        return order
    }

    companion object {
        const val ENRICHED_QUESTION: String = "ENRICHED_QUESTION"
        const val ORIGINAL_QUESTION: String = "ORIGINAL_QUESTION"
        const val EXPANSION_RATIO: String = "EXPANSION_RATIO"
    }

}