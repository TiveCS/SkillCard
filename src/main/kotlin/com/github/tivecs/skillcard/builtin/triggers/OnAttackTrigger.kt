package com.github.tivecs.skillcard.builtin.triggers

import com.github.tivecs.skillcard.core.triggers.Trigger
import com.github.tivecs.skillcard.core.triggers.TriggerAttribute
import com.github.tivecs.skillcard.core.triggers.TriggerExecuteResultState
import com.github.tivecs.skillcard.core.triggers.TriggerResult
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent

data class OnAttackTriggerAttribute(
    val attacker: LivingEntity,
    val event: EntityDamageByEntityEvent) : TriggerAttribute {

    companion object {
        const val ATTACKER_KEY = "attacker"
        const val EVENT_KEY = "event"
    }

    override fun toMutableMap(): MutableMap<String, Any> {
        return mutableMapOf(
            ATTACKER_KEY to this.attacker,
            EVENT_KEY to this.event,
        )
    }

}

object OnAttackTrigger : Trigger<EntityDamageByEntityEvent, OnAttackTriggerAttribute> {
    override val identifier: String = "on_attack"

    const val TARGET_ATTACKER_KEY = "attacker"
    private val AVAILABLE_TARGETS = setOf(TARGET_ATTACKER_KEY)

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

    override fun getTarget(
        result: TriggerResult<*>,
        type: String
    ): Any? {
        if (result.attributes == null || result.attributes !is OnAttackTriggerAttribute) return null

        return when(type) {
            TARGET_ATTACKER_KEY -> result.attributes.attacker
            else -> null
        }
    }

    override fun getAvailableTargets(): Set<String> {
        return AVAILABLE_TARGETS
    }
}