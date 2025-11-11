package com.skwmium.ragsample.repository

import com.skwmium.ragsample.entity.LoadedDocumentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface LoadedDocumentRepository : JpaRepository<LoadedDocumentEntity, Long> {
    fun existsByFileNameAndContentHash(fileName: String, contentHash: String): Boolean
}