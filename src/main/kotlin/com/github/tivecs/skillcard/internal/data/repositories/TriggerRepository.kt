package com.github.tivecs.skillcard.internal.data.repositories

import com.github.tivecs.skillcard.core.triggers.Trigger
import org.bukkit.event.Event

object TriggerRepository {

    val registeredTriggers = mutableMapOf<String, Trigger<*, *>>()

    fun register(vararg triggers: Trigger<*, *>) {
        triggers.forEach { trigger ->
            if (registeredTriggers.containsKey(trigger.identifier))
                throw IllegalArgumentException("Trigger with identifier '${trigger.identifier}' already exists.")

            registeredTriggers[trigger.identifier] = trigger
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <TEvent, TAttribute> get(identifier: String): Trigger<TEvent, TAttribute> where TEvent : Event {
        val trigger = registeredTriggers[identifier]
            ?: throw IllegalArgumentException("Trigger with identifier '$identifier' not exists.")

        return trigger as Trigger<TEvent, TAttribute>
    }
}
