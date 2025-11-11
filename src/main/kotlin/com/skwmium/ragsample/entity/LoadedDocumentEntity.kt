package com.skwmium.ragsample.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "loaded_documents")
class LoadedDocumentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loaded_documents_id_generator")
    @SequenceGenerator(name = "loaded_documents_id_generator", sequenceName = "loaded_documents_pkey_sequence", allocationSize = 1)
    val id: Long = 0,

    @Column(name = "file_name", nullable = false, length = 255)
    val fileName: String,

    @Column(name = "content_hash", nullable = false, length = 64)
    val contentHash: String,

    @Column(name = "document_type", nullable = false, length = 16)
    val documentType: String,

    @Column(name = "chunk_count", nullable = false)
    val chunkCount: Int,

    @CreationTimestamp
    @Column(name = "loaded_at", nullable = false, updatable = false)
    val loadedAt: OffsetDateTime = OffsetDateTime.MIN,
)