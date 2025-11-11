package com.skwmium.ragsample.model.mapper

import com.skwmium.ragsample.adapter.ai.AiMessageFactory
import com.skwmium.ragsample.entity.ChatEntryEntity
import com.skwmium.ragsample.model.Role
import org.springframework.ai.chat.messages.Message
import org.springframework.stereotype.Component

@Component
class ChatEntryEntityToMessageMapper(
    private val aiMessageFactories: Map<Role, AiMessageFactory>,
) : (ChatEntryEntity) -> Message {
    override fun invoke(entry: ChatEntryEntity): Message {
        return aiMessageFactories[entry.role]?.create(entry.content)
            ?: error("No MessageFactory for ${entry.role}")
    }
}