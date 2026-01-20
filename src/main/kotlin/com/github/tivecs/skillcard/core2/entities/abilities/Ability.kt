package com.github.tivecs.skillcard.core2.entities.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResultState
import com.github.tivecs.skillcard.core.abilities.AbilityRequirement
import com.github.tivecs.skillcard.core2.entities.triggers.TriggerExecutionResult
import com.github.tivecs.skillcard.internal.extensions.capitalizeWords
import org.bukkit.Material
import org.bukkit.event.Event

interface Ability<TAttribute : AbilityAttribute> {

    val identifier: String
    val targetRequirement: AbilityRequirement
    val configurableRequirements: List<AbilityRequirement>

    val displayName: String
        get() = identifier.replace("_", " ").trim().capitalizeWords()

    val material: Material
        get() = XMaterial.ENCHANTED_BOOK.get() ?: Material.ENCHANTED_BOOK

    val description: String
        get() = "&fNo description provided."

    fun execute(attribute: TAttribute?): AbilityExecuteResultState

    fun <TEvent : Event> createAttribute(abilityAttributeMap: Map<String, Any>, triggerTarget: Any?, triggerExecutionResult: TriggerExecutionResult): TAttribute?

    fun getRequirements(): List<AbilityRequirement> {
        return configurableRequirements
    }
}