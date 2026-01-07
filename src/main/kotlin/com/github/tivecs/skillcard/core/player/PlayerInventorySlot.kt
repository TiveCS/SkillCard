package com.github.tivecs.skillcard.core.player

import com.github.tivecs.skillcard.core.books.SkillBook
import com.github.tivecs.skillcard.internal.data.tables.PlayerInventorySlotTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.UUIDEntity
import org.jetbrains.exposed.v1.dao.UUIDEntityClass
import java.util.*

class PlayerInventorySlot(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PlayerInventorySlot>(PlayerInventorySlotTable)

    var slotIndex by PlayerInventorySlotTable.slotIndex
    var locked by PlayerInventorySlotTable.locked
    var bookId by PlayerInventorySlotTable.bookId

    var inventory by PlayerInventory referencedOn PlayerInventorySlotTable.inventory
}