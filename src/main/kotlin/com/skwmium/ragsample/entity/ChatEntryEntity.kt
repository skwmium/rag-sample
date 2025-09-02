package com.skwmium.ragsample.entity

import com.skwmium.ragsample.model.Role
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "chat_entries")
class ChatEntryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_entries_id_generator")
    @SequenceGenerator(name = "chat_entries_id_generator", sequenceName = "chat_entries_pkey_sequence", allocationSize = 1)
    val id: Long = 0,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.MIN,

    @Column(name = "content")
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    val role: Role,
)