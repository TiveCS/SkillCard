package com.github.tivecs.skillcard.core.books

import com.github.tivecs.skillcard.core.triggers.Trigger
import com.github.tivecs.skillcard.core.triggers.TriggerExecutionContext
import com.github.tivecs.skillcard.core.triggers.TriggerResult
import org.bukkit.entity.Player
import org.bukkit.event.Event

data class SkillBookExecutionContext<TEvent : Event>(
    val executionOrder: Int,
    val triggerContext: TriggerExecutionContext<TEvent>
) {
    fun getExecutor(): Player {
        return triggerContext.executor
    }

    fun getTriggerResult(): TriggerResult<*> {
        return triggerContext.triggerResult
    }

    fun getTrigger(): Trigger<TEvent, *> {
        return triggerContext.trigger
    }
}