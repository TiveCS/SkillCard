package com.github.tivecs.skillcard.core.triggers

import org.bukkit.event.Event

interface Trigger<TEvent, TAttribute> where TEvent : Event, TAttribute : TriggerAttribute {

    val identifier: String

    fun execute(event: TEvent) : TriggerResult<TAttribute>

    fun <TResult> getTarget(result: TriggerResult<TAttribute>, type: String): TResult?
}