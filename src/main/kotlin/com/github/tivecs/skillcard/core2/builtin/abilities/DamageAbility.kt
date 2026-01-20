package com.github.tivecs.skillcard.core2.builtin.abilities

import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResultState
import com.github.tivecs.skillcard.core.abilities.AbilityRequirement
import com.github.tivecs.skillcard.core.abilities.RequirementSource
import com.github.tivecs.skillcard.core.converters.TypeConverters
import com.github.tivecs.skillcard.core2.entities.abilities.Ability
import com.github.tivecs.skillcard.core2.entities.triggers.TriggerExecutionResult
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event

data class DamageAbilityAttribute(
    val amount: Double,
    val target: LivingEntity
) : AbilityAttribute {

}

class DamageAbility : Ability<DamageAbilityAttribute> {

    companion object {
        const val REQUIREMENT_AMOUNT_KEY = "amount"
        const val REQUIREMENT_TARGET_KEY = "target"
    }

    override val identifier: String = "damage"

    override val targetRequirement: AbilityRequirement = AbilityRequirement(
        key = REQUIREMENT_TARGET_KEY,
        targetType = LivingEntity::class,
        source = RequirementSource.TRIGGER,
    )

    override val configurableRequirements: List<AbilityRequirement> = listOf(
        AbilityRequirement(
            key = REQUIREMENT_AMOUNT_KEY,
            targetType = Double::class,
            source = RequirementSource.USER_CONFIGURED,
            defaultValue = 2.0,
        )
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

        val amount = abilityAttributeMap[REQUIREMENT_AMOUNT_KEY] as? Double ?: return null

        if (!TypeConverters.canConvert(triggerTarget, targetRequirement.targetType)) return null

        val target = TypeConverters.convert(triggerTarget, targetRequirement.targetType) ?: return null

        return DamageAbilityAttribute(
            amount,
            target as LivingEntity
        )
    }

}