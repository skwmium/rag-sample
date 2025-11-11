package com.skwmium.ragsample.adapter.ai.factory

import com.skwmium.ragsample.adapter.ai.AiMessageFactory
import com.skwmium.ragsample.model.Role
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.stereotype.Component

@Component
class UserAiMessageFactory : AiMessageFactory {
    override val role = Role.USER
    override fun create(content: String): Message = UserMessage(content)
}