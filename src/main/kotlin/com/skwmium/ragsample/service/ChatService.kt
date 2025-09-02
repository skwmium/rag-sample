package com.skwmium.ragsample.service

import com.skwmium.ragsample.entity.ChatEntity
import com.skwmium.ragsample.repository.ChatRepository
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatRepository: ChatRepository,
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
}