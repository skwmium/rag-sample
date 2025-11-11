package com.skwmium.ragsample.adapter.ai.factory

import com.skwmium.ragsample.adapter.ai.AiMessageFactory
import com.skwmium.ragsample.model.Role
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.stereotype.Component

@Component
class SystemAiMessageFactory : AiMessageFactory {
    override val role = Role.SYSTEM
    override fun create(content: String) = SystemMessage(content)
}