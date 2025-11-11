package com.skwmium.ragsample.adapter.ai.factory

import com.skwmium.ragsample.adapter.ai.RoleFactory
import com.skwmium.ragsample.model.Role
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.stereotype.Component

@Component
class AssistantRoleFactory : RoleFactory {
    override val messageType = AssistantMessage::class.java
    override fun create() = Role.ASSISTANT
}