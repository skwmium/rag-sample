package com.skwmium.ragsample.repository

import com.skwmium.ragsample.entity.ChatEntryEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ChatEntryRepository : JpaRepository<ChatEntryEntity, Long> {
    fun findByChatIdOrderByCreatedAtDesc(
        chatId: Long,
        pageable: Pageable,
    ): Page<ChatEntryEntity>
}
