package com.github.tivecs.skillcard.internal.exceptions

import com.github.tivecs.skillcard.core.entities.skills.SkillAbility

/**
 * Exception thrown when SkillAbility.ability is not initialized yet, which expected filled during runtime
 */
class SkillAbilityObjectNotInitialized(skillAbility: SkillAbility) : Exception() {
    override val message: String = "Ability object on skillId: '${skillAbility.skill.identifier}' with ability '${skillAbility.abilityIdentifier}' not initialized."
}