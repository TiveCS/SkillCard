package com.github.tivecs.skillcard.internal.data.tables

import org.jetbrains.exposed.v1.core.dao.id.CompositeIdTable

object BookAbilityTable : CompositeIdTable("book_abilities") {
    val bookId = reference("book_id", BookTable.id).entityId()
    val order = integer("order").entityId()
    val abilityIdentifier = varchar("ability_identifier", 255)

    init {
        uniqueIndex(bookId, order)
    }
}
