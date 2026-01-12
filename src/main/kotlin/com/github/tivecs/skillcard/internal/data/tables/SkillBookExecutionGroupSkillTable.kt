package com.github.tivecs.skillcard.internal.data.tables

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable

object SkillBookExecutionGroupSkillTable : UUIDTable("skill_book_execution_group_skills") {
    val executionGroupId = reference("execution_group_id", SkillBookExecutionGroupTable)
    val skillId = reference("skill_id", SkillTable)
    val executionOrder = integer("execution_order")

    init {
        uniqueIndex(executionGroupId, executionOrder)
    }
}