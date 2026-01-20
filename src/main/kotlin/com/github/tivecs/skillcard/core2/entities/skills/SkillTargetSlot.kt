package com.github.tivecs.skillcard.core2.entities.skills

import kotlin.reflect.KClass

class SkillTargetSlot(val identifier: String, val skillIdentifier: String) {

    lateinit var skill: Skill
    lateinit var targetType: KClass<*>
    lateinit var targetAbilities: List<SkillAbility>

}