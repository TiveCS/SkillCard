package com.github.tivecs.skillcard.builtin.triggers

import com.github.tivecs.skillcard.core.triggers.Trigger
import com.github.tivecs.skillcard.core.triggers.TriggerExecuteResultState
import com.github.tivecs.skillcard.core.triggers.TriggerResult
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent

data class OnAttackedTriggerAttribute(
    val attacker: LivingEntity,
    val defender: LivingEntity,
    val event: EntityDamageByEntityEvent
)

object OnAttackedTrigger : Trigger<EntityDamageByEntityEvent, OnAttackedTriggerAttribute> {
    override val identifier: String = "on_attacked"

    override fun execute(event: EntityDamageByEntityEvent): TriggerResult<OnAttackedTriggerAttribute> {
        val damager = event.damager
        val defender = event.entity

        if (damager !is LivingEntity && damager !is Projectile) {
            return TriggerResult(TriggerExecuteResultState.CONDITION_NOT_MET, null)
        }

        if (defender !is LivingEntity) {
            return TriggerResult(TriggerExecuteResultState.CONDITION_NOT_MET, null)
        }

        var actualDamager: LivingEntity? = null

        when (damager) {
            is Projectile -> actualDamager = damager.shooter as LivingEntity?
            is LivingEntity -> actualDamager = damager
        }

        if (actualDamager == null)
            return TriggerResult(TriggerExecuteResultState.CONDITION_NOT_MET, null)

        return TriggerResult(
            TriggerExecuteResultState.EXECUTED,
            OnAttackedTriggerAttribute(actualDamager, defender, event)
        )
    }
}