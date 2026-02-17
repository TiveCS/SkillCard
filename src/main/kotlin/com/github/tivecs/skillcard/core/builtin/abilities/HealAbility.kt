package com.github.tivecs.skillcard.core.builtin.abilities

import com.github.tivecs.skillcard.core.converters.TypeConverters
import com.github.tivecs.skillcard.core.entities.abilities.Ability
import com.github.tivecs.skillcard.core.entities.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.entities.abilities.AbilityExecuteResultState
import com.github.tivecs.skillcard.core.entities.abilities.AbilityRequirement
import com.github.tivecs.skillcard.core.entities.abilities.RequirementSource
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResult
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import kotlin.math.max

data class HealAbilityAttribute(
    val target: LivingEntity,
    val amount: Double
) : AbilityAttribute {}

object HealAbility : Ability<HealAbilityAttribute> {

    val REQUIREMENT_TARGET = AbilityRequirement(
        key = "target",
        source = RequirementSource.TRIGGER,
        targetType = LivingEntity::class
    )

    val REQUIREMENT_AMOUNT = AbilityRequirement(
        key = "amount",
        source = RequirementSource.USER_CONFIGURED,
        targetType = Double::class,
        defaultValue = 2.0
    )

    override val identifier: String = "heal"
    override val targetRequirement: AbilityRequirement = REQUIREMENT_TARGET
    override val configurableRequirements: List<AbilityRequirement> = listOf(
        REQUIREMENT_AMOUNT
    )

    override fun execute(attribute: HealAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE

        val target = attribute.target
        val amount = attribute.amount

        if (target.isDead) return AbilityExecuteResultState.CONDITION_NOT_MET

        val maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: return AbilityExecuteResultState.CONDITION_NOT_MET
        val newHealth = (target.health + amount).coerceAtMost(maxHealth)
        target.health = newHealth

        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(
        abilityAttributeMap: Map<String, Any>,
        triggerTarget: Any?,
        triggerExecutionResult: TriggerExecutionResult
    ): HealAbilityAttribute? {
        if (triggerTarget == null) return null

        val amount = abilityAttributeMap.getOrDefault(REQUIREMENT_AMOUNT.key, REQUIREMENT_AMOUNT.defaultValue) as? Double ?: return null
        val target = TypeConverters.convert(triggerTarget, REQUIREMENT_TARGET.targetType) as? LivingEntity ?: return null

        return HealAbilityAttribute(target, amount)
    }
}