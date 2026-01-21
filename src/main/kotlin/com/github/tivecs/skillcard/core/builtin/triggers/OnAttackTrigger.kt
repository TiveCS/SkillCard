package com.github.tivecs.skillcard.core.builtin.triggers

import com.github.tivecs.skillcard.core.entities.triggers.AvailableTriggerTarget
import com.github.tivecs.skillcard.core.entities.triggers.Trigger
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResult
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent

object OnAttackTrigger : Trigger<EntityDamageByEntityEvent>() {
    const val AVAILABLE_TARGET_ATTACKER = "attacker"
    const val AVAILABLE_TARGET_DEFENDER = "defender"

    override val identifier: String = "on_attack"

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