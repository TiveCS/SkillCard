package com.github.tivecs.skillcard.internal.data.repositories

import com.github.tivecs.skillcard.core.entities.skills.Skill

object SkillRepository {

    val cached = mutableMapOf<String, Skill>()

    fun create(skill: Skill, saveToDatabase: Boolean = true) {
        if (cached.containsKey(skill.identifier)) {
            throw IllegalArgumentException("Skill with identifier ${skill.identifier} already exists.")
        }

        if (saveToDatabase) {

        }else {
            cached[skill.identifier] = skill
        }
    }

    fun update(skill: Skill) {

    }

    fun delete(skill: Skill) {

    }

    fun findByIdentifier(identifier: String, fromCacheIfExists: Boolean = true): Skill? {
        if (fromCacheIfExists && cached.containsKey(identifier)) {
            return cached[identifier]
        }

        var skill: Skill? = null

        if (skill == null) return null

        cached[identifier] = skill

        return skill
    }

    fun findAllIdentifiers(): List<String> {
        return cached.keys.toList()
    }

    fun loadAll() {

    }

    private fun mapToSkill(data: Any): Skill {
        TODO()
    }

}