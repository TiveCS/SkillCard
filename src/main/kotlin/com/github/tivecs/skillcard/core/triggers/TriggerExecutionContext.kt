package com.github.tivecs.skillcard.core.triggers

import com.github.tivecs.skillcard.core.player.PlayerInventorySlot
import org.bukkit.entity.Player
import org.bukkit.event.Event

class TriggerExecutionContext<TEvent : Event>(
    val executor: Player,
    val targetSlots: List<PlayerInventorySlot>,
    val trigger: Trigger<TEvent, *>,
    val triggerResult: TriggerResult<*>,
    val event: TEvent
) {

}