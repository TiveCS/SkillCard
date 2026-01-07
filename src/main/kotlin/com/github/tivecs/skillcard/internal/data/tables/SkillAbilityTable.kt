package com.github.tivecs.skillcard.internal.data.tables

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable

object SkillAbilityTable : UUIDTable("skill_abilities") {
    val skillId = reference("skill_id", SkillTable.id)
    val abilityId = varchar("ability_id", 255)
    val executionOrder = integer("execution_order")
}