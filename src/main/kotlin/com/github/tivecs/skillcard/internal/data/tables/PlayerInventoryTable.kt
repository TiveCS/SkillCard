package com.github.tivecs.skillcard.internal.data.tables

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable

object PlayerInventoryTable : UUIDTable("player_inventories") {
    val playerId = uuid("player_id").uniqueIndex()

    val slots = reference("slots", PlayerInventorySlotTable)
}