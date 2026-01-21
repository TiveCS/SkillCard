package com.github.tivecs.skillcard.internal.data.repositories

import com.github.tivecs.skillcard.core.entities.triggers.Trigger
import org.bukkit.event.Event
import java.lang.reflect.ParameterizedType

object TriggerRepository {

    val registeredTriggers = mutableMapOf<String, Trigger<*>>()
    val groupedTriggersByEvent = mutableMapOf<Class<out Event>, List<Trigger<*>>>()

    fun register(vararg triggers: Trigger<*>) {
        triggers.forEach { trigger ->
            if (registeredTriggers.containsKey(trigger.identifier))
                throw IllegalArgumentException("Trigger with identifier '${trigger.identifier}' already exists.")

            registeredTriggers[trigger.identifier] = trigger

            // Extract event class from Trigger<TEvent, TAttribute> generic parameter using reflection
            val triggerInterface = trigger.javaClass.genericInterfaces
                .filterIsInstance<ParameterizedType>()
                .firstOrNull { (it.rawType as? Class<*>)?.name?.contains("Trigger") == true }

            if (triggerInterface != null) {
                @Suppress("UNCHECKED_CAST")
                val eventClass = triggerInterface.actualTypeArguments[0] as Class<out Event>
                val existing = groupedTriggersByEvent[eventClass] ?: emptyList()
                groupedTriggersByEvent[eventClass] = existing + trigger
            } else {
                throw IllegalStateException("Could not extract event class from trigger '${trigger.identifier}'")
            }
        }
    }
}
