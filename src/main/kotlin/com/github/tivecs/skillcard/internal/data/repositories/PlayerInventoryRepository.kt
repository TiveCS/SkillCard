package com.github.tivecs.skillcard.internal.data.repositories

import com.github.tivecs.skillcard.SkillCardPlugin
import com.github.tivecs.skillcard.core.player.PlayerInventory
import com.github.tivecs.skillcard.internal.data.tables.PlayerInventoryTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.*

object PlayerInventoryRepository {
    fun getOrCreate(playerId: UUID): PlayerInventory {
        SkillCardPlugin.database.connect()

        var inventory: PlayerInventory

        val founds = PlayerInventory.find { PlayerInventoryTable.playerId eq playerId }

        if (!founds.empty())
        {
            inventory = founds.first()
        }else {
            inventory = PlayerInventory.new {
                this.playerId = playerId
            }
        }

        return inventory
    }

    fun save(inventory: PlayerInventory) {
        SkillCardPlugin.database.connect()

        inventory.storeWrittenValues()
    }

}