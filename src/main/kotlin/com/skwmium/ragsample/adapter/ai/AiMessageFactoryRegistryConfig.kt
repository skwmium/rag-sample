package com.skwmium.ragsample.adapter.ai

import com.skwmium.ragsample.model.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class AiMessageFactoryRegistryConfig {
    @Bean
    fun aiMessageFactoryRegistry(factories: List<AiMessageFactory>): Map<Role, AiMessageFactory> {
        val byRole = EnumMap<Role, AiMessageFactory>(Role::class.java)
        factories.forEach { factory ->
            check(byRole.putIfAbsent(factory.role, factory) == null) {
                "Duplicate MessageFactory for role=${factory.role}: ${byRole[factory.role]} and $factory"
            }
        }
        Role.entries.forEach {
            check(byRole.containsKey(it)) {
                "No MessageFactory for role=$it"
            }
        }
        return Collections.unmodifiableMap(byRole)
    }
}