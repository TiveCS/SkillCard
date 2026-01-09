package com.github.tivecs.skillcard.core.player

import org.bukkit.entity.Player
import java.util.UUID

class PlayerInventory(val inventoryId: UUID, val playerId: UUID, slotDataList: List<PlayerInventorySlot> = emptyList()) {

    val slots: MutableMap<Int, PlayerInventorySlot> = mutableMapOf(
        *slotDataList.map { slotData -> slotData.slotIndex to slotData }.toTypedArray()
    )

    fun equip(slot: Int, bookId: UUID) {
        val slotData = this.slots[slot] ?: PlayerInventorySlot(UUID.randomUUID(), inventoryId, slot)

        slots[slot] = slotData

        if (slotData.bookId != null) {
            return
        }

        slotData.bookId = bookId
    }

    fun unequip(slot: Int, executor: Player?) {
        val slotData = slots[slot]

        if (slotData == null || slotData.bookId == null)
            return

        if (executor == null)
            return
    }

}