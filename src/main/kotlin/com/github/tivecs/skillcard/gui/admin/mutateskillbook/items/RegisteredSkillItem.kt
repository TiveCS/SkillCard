package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.entities.skills.Skill
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class RegisteredSkillItem(
    val skill: Skill,
    val onClick: ((player: Player, skill: Skill) -> Unit) = { _, _ -> },
    val displayName: (skill: Skill) -> String = { "&e${it.displayName}" },
    val displayLores: (skill: Skill) -> List<String> = { skill.getDescription() },
    val fallbackMaterial: Material = XMaterial.BOOK.get() ?: Material.BOOK,
    val displayMaterial: Material? = null
) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        onClick(player, skill)
    }

    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(displayMaterial ?: skill.material.get() ?: fallbackMaterial)
            .setDisplayName(displayName(skill).colorized())
            .setLegacyLore(displayLores(skill).map { it.colorized() })
    }
}