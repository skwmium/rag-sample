package com.skwmium.ragsample.model

enum class Role(private val role: String) {
    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system");

    companion object {
        fun fromString(role: String): Role {
            return entries.firstOrNull { it.role == role } ?: error("No role matching $role")
        }
    }
}