package com.skwmium.ragsample.service

import com.skwmium.ragsample.entity.ChatEntity
import com.skwmium.ragsample.entity.ChatEntryEntity
import com.skwmium.ragsample.model.Role
import com.skwmium.ragsample.repository.ChatRepository
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    @param:Qualifier("chatClient") private val chatClient: ChatClient,
) {

    fun getAllChats(): List<ChatEntity> {
        return chatRepository.findAll(Sort.by(DESC, "createdAt")).toList()
    }

    fun getChat(chatId: Long): ChatEntity? {
        return chatRepository.findByIdOrNull(chatId)
    }

    fun createChat(title: String): ChatEntity {
        val chatEntity = ChatEntity(title = title)
        return chatRepository.save(chatEntity)
    }

    fun deleteChat(chatId: Long) {
        chatRepository.deleteById(chatId)
    }

    fun proceedInteraction(chatId: Long, prompt: String) {
        addChatEntry(chatId, prompt, Role.USER)

        val answer = chatClient.prompt()
            .user(prompt)
            .call()
            .content()
            ?: "Empty answer"
        addChatEntry(chatId, answer, Role.ASSISTANT)
    }

    fun proceedInteractionWithStreaming(chatId: Long, prompt: String): SseEmitter {
        val answer = StringBuilder()
        val sseEmitter = SseEmitter(0L)

        chatClient.prompt(prompt)
            .advisors { it.param(ChatMemory.CONVERSATION_ID, chatId) }
            .stream()
            .chatResponse()
            .subscribe(
                { processEntry(it, sseEmitter, answer) },
                sseEmitter::completeWithError,
                sseEmitter::complete,
            )
        return sseEmitter
    }

    private fun processEntry(response: ChatResponse, sseEmitter: SseEmitter, answer: StringBuilder) {
        val token = response.result.output
        sseEmitter.send(token)
        answer.append(token.text)
    }

    private fun addChatEntry(chatId: Long, prompt: String, role: Role) {
        val entryEntity = ChatEntryEntity(
            content = prompt,
            role = role,
        )
        chatRepository.findById(chatId)
            .orElseThrow()
            .apply { addEntry(entryEntity) }
            .also { chatRepository.save(it) }

    }
}