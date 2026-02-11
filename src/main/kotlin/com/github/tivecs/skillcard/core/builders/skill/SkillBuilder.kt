package com.github.tivecs.skillcard.core.builders.skill

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.entities.skills.Skill
import com.github.tivecs.skillcard.core.entities.skills.SkillAbility
import com.github.tivecs.skillcard.core.entities.skills.SkillTargetSlot
import com.github.tivecs.skillcard.internal.extensions.colorized

class SkillBuilder {

    var identifier: String = ""
    var material: XMaterial = XMaterial.BOOK
    var description: String? = null
    var displayName: String = "New Skill"

    val abilities = arrayListOf<SkillAbility>()
    var abilityBuilder: SkillAbilityBuilder? = null

    val targetSlots = arrayListOf<SkillTargetSlot>()
    var targetSlotBuilder: SkillTargetSlotBuilder? = null

    var existingSkill: Skill? = null

    constructor()

    constructor(skill: Skill) {
        this.existingSkill = skill
        this.identifier = skill.identifier
        this.material = skill.material
        this.description = skill.description
        this.displayName = skill.displayName

        this.abilities.addAll(skill.abilities)
        this.targetSlots.addAll(skill.targetSlots)
    }

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

    fun getDescriptionDisplay(): List<String> {
        return when (description) {
            null, "" -> emptyList()
            else -> description!!.split("\n").map { it.colorized() }
        }
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

    fun validate(): List<String> {
        val errors = arrayListOf<String>()

        if (identifier.isBlank()) {
            errors.add("Identifier cannot be blank")
        }

        if (abilities.isEmpty()) {
            errors.add("Abilities cannot be empty")
        }

        if (targetSlots.isEmpty()) {
            errors.add("Target slot cannot be empty")
        }

        return errors
    }

    fun build(): Skill {
        val errors = validate()

        if (errors.isNotEmpty()) {
            throw IllegalStateException("There are missing or invalid fields during build Skill. Errors: $errors")
        }

        val currentExistingSkill = existingSkill
        if (currentExistingSkill != null) {
            currentExistingSkill.identifier = identifier
            currentExistingSkill.material = material
            currentExistingSkill.displayName = displayName
            currentExistingSkill.description = description ?: ""

            currentExistingSkill.abilities.clear()
            currentExistingSkill.abilities.addAll(abilities)
            currentExistingSkill.abilities.forEach { it.skill = currentExistingSkill }

            currentExistingSkill.targetSlots.clear()
            currentExistingSkill.targetSlots.addAll(targetSlots)
            currentExistingSkill.targetSlots.forEach {
                it.skillIdentifier = identifier
                it.skill = currentExistingSkill
            }

            return currentExistingSkill
        }

        return Skill().apply {
            identifier = this@SkillBuilder.identifier
            displayName = this@SkillBuilder.displayName
            description = this@SkillBuilder.description ?: ""
            material = this@SkillBuilder.material

            abilities.addAll(this@SkillBuilder.abilities)
            targetSlots.addAll(this@SkillBuilder.targetSlots)

            targetSlots.forEach {
                it.skillIdentifier = this@SkillBuilder.identifier
                it.skill = this
            }

            abilities.forEach {
                it.skill = this
            }
        }
    }

}