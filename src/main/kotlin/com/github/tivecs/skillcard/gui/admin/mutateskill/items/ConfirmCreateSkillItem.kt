package com.github.tivecs.skillcard.gui.admin.mutateskill.items

import com.github.tivecs.skillcard.core.builders.skill.SkillBuilder
import com.github.tivecs.skillcard.core.entities.skills.Skill
import com.github.tivecs.skillcard.gui.admin.mutateskill.MutateSkillMenu
import com.github.tivecs.skillcard.internal.data.repositories.SkillRepository
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class ConfirmCreateSkillItem(
    val builder: SkillBuilder,
    val onPostCreate: (skill: Skill) -> Unit = { }
) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {

        val errors = builder.validate()

        if (errors.isNotEmpty()) return

        val skill = builder.build()
        onPostCreate.invoke(skill)
    }

    override fun getItemProvider(): ItemProvider {
        val errors = builder.validate()
        val isValid = errors.isEmpty()

        val result = ItemBuilder(if (isValid) Material.DIAMOND else Material.BARRIER)
            .setDisplayName("&eConfirm Create New Skill".colorized())

        if (!isValid) {
            result.addLoreLines(" ")
            errors.forEach {
                result.addLoreLines("&7- &c${it}".colorized())
            }
        }

        return result
    }
}