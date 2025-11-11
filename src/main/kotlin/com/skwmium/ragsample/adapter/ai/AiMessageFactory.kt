package com.skwmium.ragsample.adapter.ai

import com.skwmium.ragsample.model.Role
import org.springframework.ai.chat.messages.Message

interface AiMessageFactory {
    val role: Role
    fun create(content: String): Message
}