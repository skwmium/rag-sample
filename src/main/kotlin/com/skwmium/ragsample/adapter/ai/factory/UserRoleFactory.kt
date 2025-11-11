package com.skwmium.ragsample.adapter.ai.factory

import com.skwmium.ragsample.adapter.ai.RoleFactory
import com.skwmium.ragsample.model.Role
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.stereotype.Component

@Component
class UserRoleFactory : RoleFactory {
    override val messageType = UserMessage::class.java
    override fun create() = Role.USER
}