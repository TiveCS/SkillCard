package com.github.tivecs.skillcard.internal.data.repositories

import com.github.tivecs.skillcard.core.skills.Skill
import com.github.tivecs.skillcard.core.skills.SkillAbility
import com.github.tivecs.skillcard.internal.data.tables.SkillAbilityTable
import com.github.tivecs.skillcard.internal.data.tables.SkillBookTable
import com.github.tivecs.skillcard.internal.data.tables.SkillTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

object SkillRepository {

    private val cache = mutableMapOf<String, Skill>()

    fun loadAll(): List<Skill> {
        TODO()
    }

    fun getAllSkillsFromCache(): Collection<Skill> {
        return cache.values
    }

    fun getAllIdentifiers(fromCacheIfExists: Boolean = true): Set<String> {
        if (fromCacheIfExists && cache.isNotEmpty()) {
            return cache.keys
        }

        val identifiers = transaction {
            SkillTable.selectAll()
                .map { it[SkillTable.identifier] }
                .toSet()
        }

        return identifiers
    }

    fun getByIdentifier(identifier: String, fromCacheIfExists: Boolean = true): Skill? {
        var skill: Skill? = null
        val abilities = mutableListOf<SkillAbility>()

        if (fromCacheIfExists) {
            skill = cache[identifier]

            if (skill != null) return skill
        }

        transaction {
            skill = SkillTable.selectAll()
                .where { SkillTable.identifier eq identifier }
                .map {
                    Skill(it[SkillTable.id].value).apply {
                        this.identifier = it[SkillTable.identifier]
                    }
                }
                .singleOrNull()

            if (skill == null) return@transaction

            val foundAbilities = SkillAbilityTable.selectAll()
                .where { SkillAbilityTable.skillId eq skill!!.skillId }
                .map {
                    SkillAbility(
                        skillId = it[SkillAbilityTable.skillId].value,
                        abilityIdentifier = it[SkillAbilityTable.abilityIdentifier],
                        executionOrder = it[SkillAbilityTable.executionOrder])
                }

            abilities += foundAbilities
        }

        if (skill == null) return null

        abilities.forEach { ability ->
            ability.ability = AbilityRepository.get<Any>(ability.abilityIdentifier)
        }

        skill!!.abilities.addAll(abilities)

        cache[identifier] = skill!!

        return skill
    }

    fun saveAll() {
        transaction {

        }
    }

    fun create(skill: Skill) {
        transaction {
            val skillId = SkillTable.insertAndGetId {
                it[SkillTable.id] = skill.skillId
                it[SkillTable.identifier] = skill.identifier
                it[SkillTable.displayName] = skill.displayName
                it[SkillTable.description] = skill.description
                it[SkillTable.material] = skill.material.name
            }

            SkillAbilityTable.batchInsert(skill.abilities) { ability ->
                this[SkillAbilityTable.skillId] = skillId
                this[SkillAbilityTable.abilityIdentifier] = ability.abilityIdentifier
                this[SkillAbilityTable.executionOrder] = ability.executionOrder
                this[SkillAbilityTable.abilityAttributes] = "{}"
            }

            commit()

            cache[skill.identifier] = skill
        }
    }

    fun saveUpdate(skill: Skill) {
        transaction {
            SkillTable.update({ SkillTable.id eq skill.skillId }) {
                it[SkillTable.identifier] = skill.identifier
                it[SkillTable.material] = skill.material.name
                it[SkillTable.displayName] = skill.displayName
                it[SkillTable.description] = skill.description
            }
        }
    }

    fun delete(identifier: String, forceDelete: Boolean = false) {
        transaction {
            var isUsedInAnySkillBook = false

            if (isUsedInAnySkillBook && !forceDelete) {
                return@transaction
            }

            SkillTable.deleteWhere { SkillTable.identifier eq identifier }
            cache.remove(identifier)
        }
    }

}