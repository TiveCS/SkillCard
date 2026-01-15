package com.github.tivecs.skillcard.core.player

import com.github.tivecs.skillcard.internal.data.repositories.PlayerInventoryRepository
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerEventListener : Listener {

    @EventHandler
    fun handleOnPlayerJoin(event: PlayerJoinEvent) {
        PlayerInventoryRepository.getOrCreate(event.player.uniqueId)
    }

    @EventHandler
    fun handleOnPlayerQuit(event: PlayerQuitEvent) {
        PlayerInventoryRepository.invalidate(event.player.uniqueId)
    }

}