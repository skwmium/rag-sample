package com.skwmium.ragsample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RagSampleApplication

fun main(args: Array<String>) {
    runApplication<RagSampleApplication>(*args)
}
