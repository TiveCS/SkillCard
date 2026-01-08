package com.github.tivecs.skillcard.core.triggers

import com.github.tivecs.skillcard.internal.data.repositories.TriggerRepository
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class TriggerEventListener : Listener {

    @EventHandler
    fun handleOnAttackTrigger(event: EntityDamageByEntityEvent) {
        val triggers = TriggerRepository.getByEventClass(event.javaClass)

        triggers.forEach { trigger ->  trigger.execute(event) }
    }

}