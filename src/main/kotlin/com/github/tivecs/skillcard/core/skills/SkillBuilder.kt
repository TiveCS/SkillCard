package com.github.tivecs.skillcard.core.skills

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.internal.data.repositories.AbilityRepository
import com.github.tivecs.skillcard.internal.data.repositories.SkillRepository
import com.github.tivecs.skillcard.internal.extensions.capitalizeWords
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.entity.Player

class SkillBuilder {
    var identifier: String? = null
    var description: String? = null
    var material: XMaterial? = null
    var displayName: String? = null
    var abilities: ArrayList<String> = arrayListOf()

    fun validate(): List<String> {
        val errors = mutableListOf<String>()

        if (identifier.isNullOrEmpty()) {
            errors.add("&cIdentifier cannot be null or empty".colorized())
        }

        if (identifier != null && identifier!!.isNotEmpty()) {
            val existingIdentifier = SkillRepository.getByIdentifier(identifier!!)

            if (existingIdentifier != null) {
                errors.add("&cIdentifier '$existingIdentifier' already exists".colorized())
            }
        }

        if (displayName.isNullOrEmpty()) {
            errors.add("&cDisplay Name cannot be null or empty".colorized())
        }

        val nonExistentAbilities = AbilityRepository.getNonExistingAbilities(abilities)
        if (nonExistentAbilities.isNotEmpty()) {
            nonExistentAbilities.forEach { ability ->
                errors.add("&cAbility $ability does not exist".colorized())
            }
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
            description = description ?: ""
        )
    }
}