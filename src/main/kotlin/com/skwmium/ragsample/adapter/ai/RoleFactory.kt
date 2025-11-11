package com.skwmium.ragsample.adapter.ai

import com.skwmium.ragsample.model.Role
import org.springframework.ai.chat.messages.Message

interface RoleFactory {
    val messageType: Class<out Message>
    fun create(): Role
}