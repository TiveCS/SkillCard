package com.github.tivecs.skillcard.core.player

import com.github.tivecs.skillcard.internal.data.tables.PlayerInventorySlotTable
import com.github.tivecs.skillcard.internal.data.tables.PlayerInventoryTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.UUIDEntity
import org.jetbrains.exposed.v1.dao.UUIDEntityClass
import java.util.*

class PlayerInventory(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PlayerInventory>(PlayerInventoryTable)

    var playerId by PlayerInventoryTable.playerId

    val slots by PlayerInventorySlot referrersOn PlayerInventorySlotTable.inventory orderBy PlayerInventorySlotTable.slotIndex
}