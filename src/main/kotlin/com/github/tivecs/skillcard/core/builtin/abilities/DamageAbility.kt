package com.github.tivecs.skillcard.core.builtin.abilities

import com.github.tivecs.skillcard.core.converters.TypeConverters
import com.github.tivecs.skillcard.core.entities.abilities.*
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResult
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event

data class DamageAbilityAttribute(
    val amount: Double,
    val target: LivingEntity
) : AbilityAttribute {

}

object DamageAbility : Ability<DamageAbilityAttribute> {

    val REQUIREMENT_TARGET = AbilityRequirement(
        key = "target",
        targetType = LivingEntity::class,
        source = RequirementSource.TRIGGER,
    )

    val REQUIREMENT_AMOUNT = AbilityRequirement(
        key = "amount",
        targetType = Double::class,
        source = RequirementSource.USER_CONFIGURED,
        defaultValue = 2.0,
    )

    override val identifier: String = "damage"

    override val targetRequirement: AbilityRequirement = REQUIREMENT_TARGET

    override val configurableRequirements: List<AbilityRequirement> = listOf(
        REQUIREMENT_AMOUNT
    )

    override fun execute(attribute: DamageAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE
        if (attribute.target.isDead) return AbilityExecuteResultState.CONDITION_NOT_MET

        attribute.target.damage(attribute.amount)
        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(
        abilityAttributeMap: Map<String, Any>,
        triggerTarget: Any?,
        triggerExecutionResult: TriggerExecutionResult
    ): DamageAbilityAttribute? {
        if (triggerTarget == null) return null

        val amount = abilityAttributeMap.getOrDefault(REQUIREMENT_AMOUNT.key, REQUIREMENT_AMOUNT.defaultValue) as? Double ?: return null
        val target = TypeConverters.convert(triggerTarget, targetRequirement.targetType) as? LivingEntity ?: return null

        return DamageAbilityAttribute(
            amount,
            target
        )
    }

}