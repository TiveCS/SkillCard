package com.github.tivecs.skillcard.core2.builtin.triggers

import com.github.tivecs.skillcard.core2.entities.triggers.AvailableTriggerTarget
import com.github.tivecs.skillcard.core2.entities.triggers.Trigger
import com.github.tivecs.skillcard.core2.entities.triggers.TriggerExecutionResult
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent

class OnDamageTrigger : Trigger<EntityDamageByEntityEvent>() {

    override val identifier: String = "on_damage"

    override fun handle(event: EntityDamageByEntityEvent): TriggerExecutionResult {
        if (event.isCancelled) return conditionNotMet()
        if (event.entity.isDead) return conditionNotMet()

        return executed(event)
    }

    override fun getAvailableTargets(): List<AvailableTriggerTarget<EntityDamageByEntityEvent, *>> {
        return listOf(
            AvailableTriggerTarget(
                key = "attacker",
                outputType = LivingEntity::class,
                getOutput = { event ->
                    return@AvailableTriggerTarget when (val damager = event.damager) {
                        is LivingEntity -> damager
                        is Projectile -> damager.shooter as? LivingEntity
                        else -> null
                    }
                }
            ),
            AvailableTriggerTarget(
                key = "defender",
                outputType = LivingEntity::class,
                getOutput = { event ->
                    return@AvailableTriggerTarget when (val defender = event.entity) {
                        is LivingEntity -> defender
                        else -> null
                    }
                }
            )
        )
    }

}