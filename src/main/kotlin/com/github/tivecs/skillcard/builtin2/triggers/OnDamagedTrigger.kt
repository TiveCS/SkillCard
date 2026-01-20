package com.github.tivecs.skillcard.builtin2.triggers

import com.github.tivecs.skillcard.core2.triggers.Trigger
import com.github.tivecs.skillcard.core2.triggers.TriggerResult
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.reflect.KClass

/**
 * Trigger that activates when player is damaged by an entity
 *
 * Available targets:
 * - "damaged": The entity receiving damage (LivingEntity) - usually the player
 * - "damager": The entity dealing damage (LivingEntity)
 */
object OnDamagedTrigger : Trigger {

    override val identifier: String = "on_damaged"

    override val availableTargets: Map<String, KClass<*>> = mapOf(
        TARGET_DAMAGED to LivingEntity::class,
        TARGET_DAMAGER to LivingEntity::class
    )

    const val TARGET_DAMAGED = "damaged"
    const val TARGET_DAMAGER = "damager"

    override fun <TEvent : Event> execute(event: TEvent): TriggerResult {
        if (event !is EntityDamageByEntityEvent) {
            return TriggerResult.failed()
        }

        val damaged = event.entity as? LivingEntity
            ?: return TriggerResult.conditionNotMet()

        val rawDamager = event.damager

        // Resolve actual damager (handle projectiles)
        val damager: LivingEntity? = when (rawDamager) {
            is Projectile -> rawDamager.shooter as? LivingEntity
            is LivingEntity -> rawDamager
            else -> null
        }

        if (damager == null) {
            return TriggerResult.conditionNotMet()
        }

        return TriggerResult.success(
            mapOf(
                TARGET_DAMAGED to damaged,
                TARGET_DAMAGER to damager
            )
        )
    }
}
