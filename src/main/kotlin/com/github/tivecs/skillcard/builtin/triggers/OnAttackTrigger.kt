package com.github.tivecs.skillcard.builtin.triggers

import com.github.tivecs.skillcard.core.triggers.Trigger
import com.github.tivecs.skillcard.core.triggers.TriggerExecuteResultState
import com.github.tivecs.skillcard.core.triggers.TriggerResult
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent

data class OnAttackTriggerAttribute(
    val attacker: LivingEntity,
    val event: EntityDamageByEntityEvent)

object OnAttackTrigger : Trigger<EntityDamageByEntityEvent, OnAttackTriggerAttribute> {
    override val identifier: String = "on_attack"

    override fun execute(event: EntityDamageByEntityEvent): TriggerResult<OnAttackTriggerAttribute> {
        val damager = event.damager

        if (damager !is LivingEntity && damager !is Projectile) {
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
            OnAttackTriggerAttribute(actualDamager, event)
        )
    }
}