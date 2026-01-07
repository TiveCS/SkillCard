package com.github.tivecs.skillcard.internal.data.tables

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable

object SkillTable : UUIDTable("skills") {
    val identifier = varchar("identifier", 255).uniqueIndex()
    val displayName = varchar("displayName", 255)
    val description = text("description").nullable()
    val material = varchar("material", 255)
}