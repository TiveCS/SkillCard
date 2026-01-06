package com.github.tivecs.skillcard.builtin.triggers

import com.github.tivecs.skillcard.core.triggers.Trigger
import com.github.tivecs.skillcard.core.triggers.TriggerResult
import org.bukkit.event.entity.EntityDamageByEntityEvent

class OnAttackTrigger : Trigger<EntityDamageByEntityEvent> {
    override val identifier: String = "on_attack"

    override fun execute(event: EntityDamageByEntityEvent): TriggerResult {
        return TriggerResult.EXECUTED
    }

}