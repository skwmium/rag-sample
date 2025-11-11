package com.skwmium.ragsample.adapter.ai.factory

import com.skwmium.ragsample.adapter.ai.AiMessageFactory
import com.skwmium.ragsample.model.Role
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.stereotype.Component

@Component
class AssistantAiMessageFactory : AiMessageFactory {
    override val role = Role.ASSISTANT
    override fun create(content: String) = AssistantMessage(content)
}