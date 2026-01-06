package com.github.tivecs.skillcard.cmds

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class SkillCardCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String?>?
    ): Boolean {

        if (args.isNullOrEmpty()) {
            return true
        }

        return true
    }
}