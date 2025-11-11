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

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    val history: MutableList<ChatEntryEntity> = mutableListOf(),
) {
    fun addEntries(chatEntryEntities: List<ChatEntryEntity>) {
        chatEntryEntities.forEach(::addEntry)
    }

    fun addEntry(chatEntryEntity: ChatEntryEntity) {
        chatEntryEntity.chat = this
        history.add(chatEntryEntity)
    }
}