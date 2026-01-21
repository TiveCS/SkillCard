package com.github.tivecs.skillcard.core2.builtin.triggers

import com.github.tivecs.skillcard.core2.entities.triggers.AvailableTriggerTarget
import com.github.tivecs.skillcard.core2.entities.triggers.Trigger
import com.github.tivecs.skillcard.core2.entities.triggers.TriggerExecutionResult
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent

class OnDamageTrigger : Trigger<EntityDamageByEntityEvent>() {

    companion object {
        const val AVAILABLE_TARGET_ATTACKER = "attacker"
        const val AVAILABLE_TARGET_DEFENDER = "defender"
    }

    override val identifier: String = "on_damage"

    override val availableTargets: List<AvailableTriggerTarget<EntityDamageByEntityEvent, *>> = listOf(
        AvailableTriggerTarget(
            key = AVAILABLE_TARGET_ATTACKER,
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
            key = AVAILABLE_TARGET_DEFENDER,
            outputType = LivingEntity::class,
            getOutput = { event ->
                return@AvailableTriggerTarget when (val defender = event.entity) {
                    is LivingEntity -> defender
                    else -> null
                }
            }
        )
    )

    override fun handle(event: EntityDamageByEntityEvent): TriggerExecutionResult {
        if (event.isCancelled) return conditionNotMet()
        if (event.entity.isDead) return conditionNotMet()

        return executed(event)
    }

}