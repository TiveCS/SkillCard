package com.github.tivecs.skillcard.internal.data.tables

import org.jetbrains.exposed.v1.core.dao.id.CompositeIdTable
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable

object BookTable : UUIDTable("books") {
    val identifier = varchar("identifier", 255).uniqueIndex()
    val displayName = varchar("display_name", 255)
    val material = varchar("material", 255)
    val description = text("description").nullable()
}

