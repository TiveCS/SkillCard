package com.github.tivecs.skillcard.internal.data.tables

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable

object SkillBookTable : UUIDTable("skill_books") {
    val identifier = varchar("identifier", 255).uniqueIndex()
    val displayName = varchar("display_name", 255)
    val material = varchar("material", 255)
    val description = text("description").nullable()
}

