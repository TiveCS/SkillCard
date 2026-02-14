package com.github.tivecs.skillcard.core.entities.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResult
import com.github.tivecs.skillcard.internal.extensions.capitalizeWords
import com.github.tivecs.skillcard.internal.extensions.colorized
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

    fun getDescriptionDisplay(): List<String> {
        return description.split("\n").map { line -> line.trim().colorized() }
    }
}