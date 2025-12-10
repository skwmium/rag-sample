package com.skwmium.ragsample.service

import com.skwmium.ragsample.entity.LoadedDocumentEntity
import com.skwmium.ragsample.repository.LoadedDocumentRepository
import com.skwmium.ragsample.util.extensions.md5
import org.springframework.ai.reader.TextReader
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.Resource
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.stereotype.Service

@Suppress("SameParameterValue")
@Service
class DocumentLoaderService(
    private val loadedDocumentRepository: LoadedDocumentRepository,
    private val resourcePatternResolver: ResourcePatternResolver,
    private val vectorStore: VectorStore,
) : ApplicationRunner {

    private data class LocalDocument(
        val resource: Resource,
        val fileName: String,
        val documentType: String,
        val contentHash: String,
    )

    override fun run(args: ApplicationArguments) {
        loadLocalDocuments(locationPattern = "classpath:/knowledgebase/**/*.txt", chunkSize = 200)
    }

    private fun loadLocalDocuments(locationPattern: String, chunkSize: Int) {
        resourcePatternResolver.getResources(locationPattern)
            .asSequence()
            .mapNotNull { it.toLocalDocument() }
            .filterNot { loadedDocumentRepository.existsByFileNameAndContentHash(it.fileName, it.contentHash) }
            .forEach { saveLocalTextDocument(it, chunkSize) }
    }

    private fun Resource.toLocalDocument(defaultType: String = "txt"): LocalDocument? {
        val fileName = filename ?: return null
        val documentType = fileName.substringAfterLast('.', defaultType)
        return LocalDocument(this, fileName, documentType, md5)
    }

    private fun saveLocalTextDocument(localTextDocument: LocalDocument, chunkSize: Int) {
        val documents = TextReader(localTextDocument.resource).get()
        val textSplitter = TokenTextSplitter.builder().withChunkSize(chunkSize).build()
        val chunks = textSplitter.apply(documents)

        vectorStore.accept(chunks)

        val loadedDocument = LoadedDocumentEntity(
            fileName = localTextDocument.fileName,
            contentHash = localTextDocument.contentHash,
            documentType = localTextDocument.documentType,
            chunkCount = chunks.size,
        )
        loadedDocumentRepository.save(loadedDocument)
    }
}