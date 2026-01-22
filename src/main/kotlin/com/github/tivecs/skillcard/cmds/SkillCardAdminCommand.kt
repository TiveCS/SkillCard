package com.github.tivecs.skillcard.cmds

import com.github.tivecs.skillcard.gui.admin.mutateskill.MutateSkillMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class SkillCardAdminCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String?>?
    ): Boolean {
        if (args.isNullOrEmpty()) {
            if (sender is Player) {
                MutateSkillMenu.open(sender)
            }
            return true
        }

        if (args[0] == "manage") {
            if (sender is Player) {
                MutateSkillMenu.open(sender)
            }
            return true
        }

        if (args[0] == "help" || args[0] == "?") {
            sender.sendMessage("&cSkillCard Admin Command Help".colorized())
            sender.sendMessage(" ")
            sender.sendMessage("&c/$label &f[manage] &8- &7Open SkillCard management menu".colorized())
            return true
        }

        return false
    }
}

class SkillCardAdminCommandTabCompleter : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String?>?
    ): List<String>? {
        val completions = mutableListOf<String>()
        if (args.isNullOrEmpty())
            return null

        if (args.size == 1) {
            val suggestions = listOf("help", "?", "skill", "manage")

            if (args[0] != null) {
                StringUtil.copyPartialMatches(args[0]!!, suggestions, completions)
            }

            return completions
        }

        if (args.size == 2) {
            if (args[0] == "skill") {
                val suggestions = listOf("new", "delete", "edit", "get")

                if (args[1] != null) {
                    StringUtil.copyPartialMatches(args[1]!!, suggestions, completions)
                }

                return completions
            }
        }

        if (args.size == 3 && args[0] == "skill") {
            if (args[1] == "get" || args[1] == "delete" || args[1] == "edit") {
                val skillIdentifiers = SkillRepository.getAllIdentifiers()

                StringUtil.copyPartialMatches(args[2] ?: "", skillIdentifiers, completions)

                return completions
            }
        }

        return null
    }
}