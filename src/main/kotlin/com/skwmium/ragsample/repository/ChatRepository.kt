package com.skwmium.ragsample.repository

import com.skwmium.ragsample.entity.ChatEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRepository : JpaRepository<ChatEntity, Long> {
}