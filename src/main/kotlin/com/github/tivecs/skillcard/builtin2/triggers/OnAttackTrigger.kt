package com.github.tivecs.skillcard.builtin2.triggers

import com.github.tivecs.skillcard.core2.triggers.Trigger
import com.github.tivecs.skillcard.core2.triggers.TriggerResult
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.reflect.KClass

/**
 * Trigger that activates when player attacks an entity
 *
 * Available targets:
 * - "attacker": The entity dealing damage (LivingEntity)
 * - "defender": The entity receiving damage (LivingEntity)
 */
object OnAttackTrigger : Trigger {

    override val identifier: String = "on_attack"

    override val availableTargets: Map<String, KClass<*>> = mapOf(
        TARGET_ATTACKER to LivingEntity::class,
        TARGET_DEFENDER to LivingEntity::class
    )

    const val TARGET_ATTACKER = "attacker"
    const val TARGET_DEFENDER = "defender"

    override fun <TEvent : Event> execute(event: TEvent): TriggerResult {
        if (event !is EntityDamageByEntityEvent) {
            return TriggerResult.failed()
        }

        val defender = event.entity as? LivingEntity
            ?: return TriggerResult.conditionNotMet()

        val damager = event.damager

        // Resolve actual attacker (handle projectiles)
        val attacker: LivingEntity? = when (damager) {
            is Projectile -> damager.shooter as? LivingEntity
            is LivingEntity -> damager
            else -> null
        }

        if (attacker == null) {
            return TriggerResult.conditionNotMet()
        }

        return TriggerResult.success(
            mapOf(
                TARGET_ATTACKER to attacker,
                TARGET_DEFENDER to defender
            )
        )
    }
}
