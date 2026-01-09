package com.github.tivecs.skillcard.internal.data.repositories

import com.github.tivecs.skillcard.core.player.PlayerInventory
import com.github.tivecs.skillcard.internal.data.tables.PlayerInventorySlotTable
import com.github.tivecs.skillcard.internal.data.tables.PlayerInventoryTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.insertReturning
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.*

object PlayerInventoryRepository {

    // Player ID, Inventory
    private val cache = mutableMapOf<UUID, PlayerInventory>()

    fun getOrCreate(playerId: UUID, fromCacheIfExists: Boolean = true): PlayerInventory {
        if (fromCacheIfExists) {
            val found = cache[playerId]
            if (found != null) return found
        }

        val found = PlayerInventoryTable.selectAll().where {
            PlayerInventoryTable.playerId eq playerId
        }.map {
            PlayerInventory(
                it[PlayerInventoryTable.id].value,
                it[PlayerInventoryTable.playerId]
            )
        }.singleOrNull()

        if (found != null) {
            cache[playerId] = found
            return found
        }

        val inventory: PlayerInventory = transaction {
            return@transaction PlayerInventoryTable.insertReturning {
                it[PlayerInventoryTable.playerId] = playerId
            }.map {
                PlayerInventory(it[PlayerInventoryTable.id].value, it[PlayerInventoryTable.playerId])
            }.single()
        }

        cache[playerId] = inventory

        return inventory
    }

    fun saveToDatabase(inventory: PlayerInventory) {
        transaction {
//            inventory.slots.forEach { (slotIndex, slotData) ->
//                PlayerInventorySlotTable.upsert(
//                    keys = arrayOf(PlayerInventorySlotTable.slotIndex, PlayerInventorySlotTable.inventoryId),
//                    onUpdate = {
//                        it[PlayerInventorySlotTable.locked] = slotData.locked
//                        it[PlayerInventorySlotTable.bookId] = slotData.bookId
//                    },
//                ) {
//                    it[PlayerInventorySlotTable.slotIndex] = slotIndex
//                    it[PlayerInventorySlotTable.inventoryId] = slotData.inventoryId
//                    it[PlayerInventorySlotTable.locked] = false
//                    it[PlayerInventorySlotTable.bookId] = null
//                }
//            }
            val slotsToUpsert = inventory.slots.map { (slotIndex, slotData) ->
                slotIndex to slotData
            }

            PlayerInventorySlotTable.batchUpsert(
                data = slotsToUpsert,
                keys = arrayOf(PlayerInventorySlotTable.inventoryId, PlayerInventorySlotTable.slotIndex)
            ) { (slotIndex, slotData) ->
                this[PlayerInventorySlotTable.slotIndex] = slotIndex
                this[PlayerInventorySlotTable.inventoryId] = slotData.inventoryId
                this[PlayerInventorySlotTable.locked] = slotData.locked
                this[PlayerInventorySlotTable.bookId] = slotData.bookId
            }
        }
    }

    fun invalidate(playerId: UUID) {
        cache.remove(playerId)
    }

}