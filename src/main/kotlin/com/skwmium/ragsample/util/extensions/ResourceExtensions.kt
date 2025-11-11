package com.skwmium.ragsample.util.extensions

import org.springframework.core.io.Resource
import org.springframework.util.DigestUtils

val Resource.md5: String
    get() = inputStream.use { DigestUtils.md5DigestAsHex(it) }