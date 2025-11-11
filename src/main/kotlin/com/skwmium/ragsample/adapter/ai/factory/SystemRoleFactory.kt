package com.skwmium.ragsample.adapter.ai.factory

import com.skwmium.ragsample.adapter.ai.RoleFactory
import com.skwmium.ragsample.model.Role
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.stereotype.Component

@Component
class SystemRoleFactory : RoleFactory {
    override val messageType = SystemMessage::class.java
    override fun create() = Role.SYSTEM
}