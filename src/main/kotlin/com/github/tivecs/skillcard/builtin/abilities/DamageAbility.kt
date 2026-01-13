package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.*
import com.github.tivecs.skillcard.core.triggers.TriggerAttributeKey
import org.bukkit.Material
import org.bukkit.entity.LivingEntity

data class DamageAbilityAttribute(
    val amount: Double,
    val target: LivingEntity) : AbilityAttribute {

    companion object : ConfigurableAbilityAttributes {
        const val AMOUNT_KEY = "amount"

        override fun fromMapAttributes(
            triggerAttributes: Map<String, Any>,
            skillAttributes: Map<String, Any>
        ): AbilityAttribute? {
            val target = triggerAttributes[TriggerAttributeKey.TARGET_TYPE.key] as? LivingEntity ?: return null
            val amount = skillAttributes[AMOUNT_KEY] as? Double ?: return null

            return DamageAbilityAttribute(amount, target)
        }
    }

}

object DamageAbility : Ability<DamageAbilityAttribute> {
    override val identifier: String = "damage"

    override val material: Material
        get() = XMaterial.IRON_SWORD.get() ?: Material.IRON_SWORD

    override val description: String
        get() = "&fDeals a specified amount of damage to the target entity."

    override fun execute(attribute: DamageAbilityAttribute): AbilityExecuteResult {
        if (attribute.target.isDead)
            return AbilityExecuteResult.CONDITION_NOT_MET

        attribute.target.damage(attribute.amount)

        return AbilityExecuteResult.EXECUTED
    }

    override fun mapExecutionContextToAttribute(context: Map<String, Any>): DamageAbilityAttribute? {
        TODO("Not yet implemented")
    }

}