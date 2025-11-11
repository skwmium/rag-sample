package com.skwmium.ragsample.model.mapper

import com.skwmium.ragsample.adapter.ai.RoleFactory
import com.skwmium.ragsample.entity.ChatEntryEntity
import org.springframework.ai.chat.messages.Message
import org.springframework.stereotype.Component

@Component
class MessageToChatEntryEntityMapper(
    private val roleFactories: Map<Class<out Message>, RoleFactory>,
) : (Message) -> ChatEntryEntity {

    override fun invoke(message: Message): ChatEntryEntity {
        val factory = roleFactories[message.javaClass]
            ?: error("No RoleFactory for message type: ${message::class.qualifiedName}")

        return ChatEntryEntity(
            content = message.text,
            role = factory.create(),
        )
    }
}