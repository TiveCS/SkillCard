package com.github.tivecs.skillcard.internal.data.tables

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable

object SkillBookExecutionGroupTable : UUIDTable("skill_book_execution_groups") {
    val bookId = reference("book_id", SkillBookTable)
    val triggerIdentifier = varchar("trigger_identifier", 255)
    val executionOrder = integer("execution_order")

    init {
        uniqueIndex(bookId, executionOrder)
    }
}