package com.github.tivecs.skillcard.core.player

import org.bukkit.OfflinePlayer
import java.util.*

class PlayerProfile {
    val playerId: UUID
    val inventory: PlayerInventory

    constructor(player: OfflinePlayer) {
        playerId = player.uniqueId
        inventory = PlayerInventory(this)
    }

    fun fetchFromDatabase() {}

    fun saveToDatabase() {}
}