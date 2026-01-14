package com.github.tivecs.skillcard.builtin.triggers

import com.github.tivecs.skillcard.core.triggers.Trigger
import com.github.tivecs.skillcard.core.triggers.TriggerAttribute
import com.github.tivecs.skillcard.core.triggers.TriggerExecuteResultState
import com.github.tivecs.skillcard.core.triggers.TriggerResult
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent

data class OnAttackedTriggerAttribute(
    val attacker: LivingEntity,
    val defender: LivingEntity,
    val event: EntityDamageByEntityEvent
) : TriggerAttribute {
    companion object {
        const val ATTACKER_KEY = "attacker"
        const val DEFENDER_KEY = "defender"
        const val EVENT_KEY = "event"
    }

    override fun toMutableMap(): MutableMap<String, Any> {
        return mutableMapOf(
            ATTACKER_KEY to attacker,
            DEFENDER_KEY to defender,
            EVENT_KEY to event
        )
    }
}

object OnAttackedTrigger : Trigger<EntityDamageByEntityEvent, OnAttackedTriggerAttribute> {
    override val identifier: String = "on_attacked"

    const val TARGET_ATTACKER_KEY = "attacker"
    const val TARGET_DEFENDER_KEY = "defender"
    private val AVAILABLE_TARGETS = setOf(TARGET_ATTACKER_KEY, TARGET_DEFENDER_KEY)

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

    override fun getTarget(result: TriggerResult<*>, type: String): Any? {
        if (result.attributes == null || result.attributes !is OnAttackedTriggerAttribute) return null

        return when (type) {
            TARGET_ATTACKER_KEY -> result.attributes.attacker
            TARGET_DEFENDER_KEY -> result.attributes.defender
            else -> null
        }
    }

    override fun getAvailableTargets(): Set<String> {
        return AVAILABLE_TARGETS
    }
}