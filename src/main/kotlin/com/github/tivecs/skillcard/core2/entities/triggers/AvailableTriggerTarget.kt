package com.github.tivecs.skillcard.core2.entities.triggers

import org.bukkit.event.Event
import kotlin.reflect.KClass

data class AvailableTriggerTarget<TTriggerEvent : Event, TOutputType : Any>(
    val key: String,
    val outputType: KClass<TOutputType>,
    val converterName: String? = null,
    val getOutput: (event: TTriggerEvent) -> TOutputType?
) {
}