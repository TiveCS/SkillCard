package com.github.tivecs.skillcard.core.builders

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.entities.skills.SkillAbility
import com.github.tivecs.skillcard.core.entities.skills.SkillTargetSlot

class SkillBuilder {

    var identifier: String = ""
    var material: XMaterial = XMaterial.BOOK
    var description: String? = null
    var displayName: String = "New Skill"

    val abilities = mutableListOf<SkillAbility>()
    var abilityBuilder: SkillAbilityBuilder? = null

    val targetSlots = mutableListOf<SkillTargetSlot>()
    var targetSlotBuilder: SkillTargetSlotBuilder? = null

    fun setIdentifier(id: String): SkillBuilder {
        this.identifier = id
        return this
    }

    fun setMaterial(material: XMaterial): SkillBuilder {
        this.material = material
        return this
    }

    fun setDescription(description: String?): SkillBuilder {
        this.description = description
        return this
    }

    fun setDisplayName(name: String): SkillBuilder {
        this.displayName = name
        return this
    }

    fun newTargetSlotBuilder(): SkillTargetSlotBuilder {
        val builder = SkillTargetSlotBuilder(this)
        targetSlotBuilder = builder
        return builder
    }

    fun newAbilityBuilder(): SkillAbilityBuilder {
        val builder = SkillAbilityBuilder(this)
        abilityBuilder = builder
        return builder
    }

}