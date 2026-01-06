package com.github.tivecs.skillcard.internal.data.tables

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable

object PlayerInventorySlotTable : UUIDTable("player_inventory_slots") {
    val inventoryId = reference("inventory_id", PlayerInventoryTable.id)
    val slotIndex = integer("slot_index")
    val locked = bool("locked").default(false)
    val bookId = optReference("book_id", BookTable.id)

    init {
        uniqueIndex(inventoryId, slotIndex)
    }
}