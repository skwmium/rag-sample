package com.skwmium.ragsample.service

import com.skwmium.ragsample.entity.ChatEntity
import com.skwmium.ragsample.entity.ChatEntryEntity
import com.skwmium.ragsample.model.Role
import com.skwmium.ragsample.repository.ChatRepository
import jakarta.transaction.Transactional
import org.springframework.ai.chat.client.ChatClient
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val chatClient: ChatClient,
) {

    fun getAllChats(): List<ChatEntity> {
        return chatRepository.findAll(Sort.by(DESC, "createdAt")).toList()
    }

    fun getChat(chatId: Long): ChatEntity? {
        return chatRepository.getReferenceById(chatId)
    }

    fun createChat(title: String): ChatEntity {
        val chatEntity = ChatEntity(title = title)
        return chatRepository.save(chatEntity)
    }

    fun deleteChat(chatId: Long) {
        chatRepository.deleteById(chatId)
    }

    @Transactional
    fun proceedInteraction(chatId: Long, prompt: String) {
        addChatEntry(chatId, prompt, Role.USER)

        val answer = chatClient.prompt()
            .user(prompt)
            .call()
            .content()
            ?: "Empty answer"
        addChatEntry(chatId, answer, Role.ASSISTANT)
    }

    private fun addChatEntry(chatId: Long, prompt: String, role: Role) {
        chatRepository.getReferenceById(chatId).addEntry(
            ChatEntryEntity(
                content = prompt,
                role = role,
            )
        )
    }
}