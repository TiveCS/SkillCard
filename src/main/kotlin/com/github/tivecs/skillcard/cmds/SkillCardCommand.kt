package com.github.tivecs.skillcard.cmds

import com.github.tivecs.skillcard.core.player.PlayerInventory
import com.github.tivecs.skillcard.internal.data.repositories.PlayerInventoryRepository
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.UUID

class SkillCardCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String?>?
    ): Boolean {

        val dummyPlayerId = UUID.fromString("cd6f4cfe-5dbc-4c45-8dd0-b33ccc18fee2")

        val playerInventory: PlayerInventory = PlayerInventoryRepository.getOrCreate(dummyPlayerId)

        if (args.isNullOrEmpty()) {
            sender.sendMessage("Inventory ID: ${playerInventory.id}")
            return true
        }

        return true
    }
}