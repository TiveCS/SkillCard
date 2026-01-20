package com.github.tivecs.skillcard.core2.entities.triggers

import org.bukkit.event.Event

abstract class Trigger<TEvent : Event> {

    abstract val identifier: String

    abstract fun handle(event: TEvent): TriggerExecutionResult

    abstract fun getAvailableTargets(): List<AvailableTriggerTarget<TEvent, *>>

    @Suppress("UNCHECKED_CAST")
    internal fun conditionNotMet(): TriggerExecutionResult {
        return TriggerExecutionResult(TriggerExecutionResultStatus.CONDITION_NOT_MET, this as Trigger<Event>, null, null)
    }

    @Suppress("UNCHECKED_CAST")
    internal fun executed(event: Event, additionalData: Any? = null): TriggerExecutionResult {
        return TriggerExecutionResult(TriggerExecutionResultStatus.EXECUTED,
            this as Trigger<Event>, additionalData, event)
    }

}