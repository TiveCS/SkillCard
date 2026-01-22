package com.github.tivecs.skillcard.core.entities.skills

import kotlin.reflect.KClass

class SkillTargetSlot(val identifier: String) {

    lateinit var skillIdentifier: String

    lateinit var skill: Skill
    lateinit var targetType: KClass<*>
    lateinit var targetAbilities: List<SkillAbility>

}