package com.github.tivecs.skillcard.cmds

import com.github.tivecs.skillcard.internal.data.repositories.PlayerInventoryRepository
import com.github.tivecs.skillcard.internal.extensions.colorized
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

        if (args.isNullOrEmpty()) {
            return true
        }

        if (args[0] == "help" || args[0] == "?") {
            sender.sendMessage("&7SkillCard Commands:".colorized())
            sender.sendMessage("&e[] &f: Optional argument &8| &e<> &f: Required Argument".colorized())
            sender.sendMessage("")
            sender.sendMessage("&6/skillcard &8- &fShow your skill card inventory".colorized())
            sender.sendMessage("&6/skillcard &e<help|?> &8- &fShow this help message".colorized())
            sender.sendMessage("&6/skillcard &eequip <slot> &8- &fEquip skill book item on your hand to inventory slot".colorized())
            sender.sendMessage("&6/skillcard &eunequip <slot> &8- &fUnequip inventory slot".colorized())
            sender.sendMessage("&6/skillcard &elock [slot] &8- &fToggle lock slot to prevent accidentally unequip it".colorized())
            sender.sendMessage("&6/skillcard &elock [slot] &8- &fToggle lock slot to prevent accidentally unequip it".colorized())
            return true
        }

        return false
    }
}