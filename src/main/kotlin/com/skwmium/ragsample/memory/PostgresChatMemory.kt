package com.skwmium.ragsample.memory

import com.skwmium.ragsample.entity.ChatEntryEntity
import com.skwmium.ragsample.model.mapper.ChatEntryEntityToMessageMapper
import com.skwmium.ragsample.model.mapper.MessageToChatEntryEntityMapper
import com.skwmium.ragsample.repository.ChatEntryRepository
import com.skwmium.ragsample.repository.ChatRepository
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.messages.Message
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostgresChatMemory(
    private val chatRepository: ChatRepository,
    private val chanEntryRepository: ChatEntryRepository,
    private val chatEntryEntityToMessageMapper: ChatEntryEntityToMessageMapper,
    private val messageToChatEntryEntityMapper: MessageToChatEntryEntityMapper,
) : ChatMemory {

    private val maxMessages: Int = 12

    @Transactional
    override fun add(conversationId: String, messages: List<Message>) {
        val chatEntries = messages.map(messageToChatEntryEntityMapper)
        chatRepository.findById(conversationId.toLong())
            .orElseThrow()
            .apply { addEntries(chatEntries) }
            .apply { chatRepository.save(this) }
    }

    override fun get(conversationId: String): List<Message> {
        val chatId = conversationId.toLong()
        val messages = chanEntryRepository.findLastMessages(chatId, maxMessages)
            .map(chatEntryEntityToMessageMapper)
        return messages
    }

    override fun clear(conversationId: String) {
        //not implemented
    }

    private fun ChatEntryRepository.findLastMessages(chatId: Long, count: Int): List<ChatEntryEntity> {
        return findByChatIdOrderByCreatedAtDesc(chatId, PageRequest.of(0, count))
            .content
            .asReversed()
    }
}