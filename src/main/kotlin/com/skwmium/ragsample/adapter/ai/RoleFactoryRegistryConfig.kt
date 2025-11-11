package com.skwmium.ragsample.adapter.ai

import org.springframework.ai.chat.messages.Message
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*


@Configuration
class RoleFactoryRegistryConfig {
    @Bean
    fun roleFactoryRegistry(resolvers: List<RoleFactory>): Map<Class<out Message>, RoleFactory> {
        val byType = HashMap<Class<out Message>, RoleFactory>()
        resolvers.forEach { resolver ->
            check(byType.putIfAbsent(resolver.messageType, resolver) == null) {
                "Duplicate RoleResolver for messageType=${resolver.messageType}: ${byType[resolver.messageType]} and $resolver"
            }
        }
        return Collections.unmodifiableMap(byType)
    }
}

