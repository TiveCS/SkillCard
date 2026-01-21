package com.github.tivecs.skillcard.core.entities.triggers

import org.bukkit.event.Event

data class TriggerExecutionResult(
    val status: TriggerExecutionResultStatus,
    val trigger: Trigger<Event>,
    val additionalData: Any? = null,
    val event: Event? = null
) {

}