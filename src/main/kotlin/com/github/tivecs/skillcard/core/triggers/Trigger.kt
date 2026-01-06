package com.github.tivecs.skillcard.core.triggers

import org.bukkit.event.Event

interface Trigger<TEvent> where TEvent : Event {

    val identifier: String

    fun execute(event: TEvent) : TriggerResult

}