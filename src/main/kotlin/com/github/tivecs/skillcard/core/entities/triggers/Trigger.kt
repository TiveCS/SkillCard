package com.github.tivecs.skillcard.core.entities.triggers

import org.bukkit.event.Event

abstract class Trigger<TEvent : Event> {

    abstract val identifier: String

    abstract val availableTargets: List<AvailableTriggerTarget<TEvent, *>>


    abstract fun handle(event: TEvent): TriggerExecutionResult

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