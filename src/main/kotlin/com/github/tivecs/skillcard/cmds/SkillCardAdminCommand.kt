package com.github.tivecs.skillcard.cmds

import com.github.tivecs.skillcard.core.skills.Skill
import com.github.tivecs.skillcard.internal.data.repositories.SkillRepository
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class SkillCardAdminCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String?>?
    ): Boolean {

        if (args.isNullOrEmpty()) {
            return true
        }

        if (args[0] == "help" || args[0] == "?") {
            sender.sendMessage("&cSkillCard Admin Command Help".colorized())
            sender.sendMessage(" ")
            sender.sendMessage("&c/skillcardadmin &f[manage] &8- &7Open SkillCard management menu")
            sender.sendMessage("&c/skillcardadmin skill new <skill_identifier> [...ability_identifiers] &8- &7Create new skill")
            return true
        }

        if (args.size >= 3 && args[0] == "skill" && args[1] == "new") {
            val skillIdentifier = args[3]!!
            val abilityIdentifiers = args.slice(IntRange(4, args.size)).filter { !it.isNullOrEmpty() }

            val existingSkill = SkillRepository.getByIdentifier(skillIdentifier)

            if (existingSkill != null) {
                sender.sendMessage("&eSkill '&c$skillIdentifier&e' already exist".colorized())
                return false
            }

            val newSkill = Skill.create(
                skillIdentifier,
                abilities = listOf())

            SkillRepository.create(newSkill)

            return true
        }

        return false
    }
}