package com.skwmium.ragsample.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "chats")
class ChatEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chats_id_generator")
    @SequenceGenerator(name = "chats_id_generator", sequenceName = "chats_pkey_sequence", allocationSize = 1)
    val id: Long = 0,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.MIN,

    @Column(name = "title")
    val title: String,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "chat_id")
    val history: MutableList<ChatEntryEntity> = mutableListOf(),
) {
    fun addEntry(chatEntryEntity: ChatEntryEntity) {
        history.add(chatEntryEntity)
    }
}