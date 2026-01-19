package com.github.tivecs.skillcard.core.skills

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.internal.data.repositories.AbilityRepository
import com.github.tivecs.skillcard.internal.data.repositories.SkillRepository
import com.github.tivecs.skillcard.internal.extensions.capitalizeWords
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.entity.Player
import java.util.UUID

class SkillBuilder {
    val skillId: UUID = UUID.randomUUID()

    var identifier: String? = null
        private set

    var description: String? = null
        private set

    var material: XMaterial? = null
        private set

    var displayName: String? = null
        private set

    var triggerTargetType: String? = null
        private set

    var abilities: ArrayList<SkillAbility> = arrayListOf()
        private set

    fun setIdentifier(identifier: String): SkillBuilder {
        this.identifier = identifier
        return this
    }

    fun setDescription(description: String): SkillBuilder {
        this.description = description
        return this
    }

    fun setDescription(lines: List<String>): SkillBuilder {
        this.description = lines.joinToString("\n")
        return this
    }

    fun reset(): SkillBuilder {
        this.identifier = null
        this.description = null
        this.material = null
        this.displayName = null
        this.triggerTargetType = null
        this.abilities = arrayListOf()
        return this
    }

    fun addDescriptionLine(line: String): SkillBuilder {
        if (this.description == null) {
            this.description = line
        } else {
            this.description += "\n$line"
        }
        return this
    }

    fun setMaterial(material: XMaterial): SkillBuilder {
        this.material = material
        return this
    }

    fun setDisplayName(displayName: String): SkillBuilder {
        this.displayName = displayName
        return this
    }

    fun setTriggerTargetType(triggerTargetType: String): SkillBuilder {
        this.triggerTargetType = triggerTargetType
        return this
    }

    fun setAbilities(abilities: List<SkillAbility>): SkillBuilder {
        this.abilities = ArrayList(abilities)
        return this
    }

    fun addAbilities(vararg abilities: SkillAbility): SkillBuilder {
        this.abilities.addAll(abilities)
        return this
    }

    fun validate(): List<String> {
        val errors = mutableListOf<String>()

        if (identifier.isNullOrEmpty() || identifier.isNullOrBlank()) {
            errors.add("&cIdentifier cannot be null or empty".colorized())
        }

        if (identifier != null && identifier!!.isNotEmpty()) {
            val existingIdentifier = SkillRepository.getByIdentifier(identifier!!)

            if (existingIdentifier != null) {
                errors.add("&cIdentifier '$existingIdentifier' already exists".colorized())
            }
        }

        if (displayName.isNullOrEmpty() || displayName.isNullOrBlank()) {
            errors.add("&cDisplay Name cannot be null or empty".colorized())
        }

        val nonExistentAbilities = AbilityRepository.getNonExistingAbilities(abilities.map { it.abilityIdentifier })
        if (nonExistentAbilities.isNotEmpty()) {
            nonExistentAbilities.forEach { ability ->
                errors.add("&cAbility $ability does not exist".colorized())
            }
        }

        if (triggerTargetType.isNullOrEmpty() || triggerTargetType.isNullOrBlank()) {
            errors.add("&cTrigger Target Type cannot be empty".colorized())
        }

        return errors
    }

    fun build(creator: Player): Skill? {
        val errorMessages = validate()

        if (errorMessages.isNotEmpty()) {
            errorMessages.forEach { error -> creator.sendMessage(error) }
            return null
        }

        return Skill.create(
            identifier = identifier!!,
            abilities = abilities,
            material = material ?: XMaterial.BOOK,
            displayName = displayName ?: identifier!!.trim().replace("_", " ").capitalizeWords(),
            description = description ?: "",
            triggerTargetType = triggerTargetType ?: ""
        )
    }
}