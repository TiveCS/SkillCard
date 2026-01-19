package com.github.tivecs.skillcard.gui.admin.items

import com.github.tivecs.skillcard.core.skills.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.SkillListedAbilitiesMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenSkillListedAbilitiesMenuItem(val builder: SkillBuilder, val displayText: String = "&a&lSkill Ability List") : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        SkillListedAbilitiesMenu.open(player, builder)
    }

    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(Material.ENCHANTING_TABLE)
            .setDisplayName(displayText.colorized())
            .addLoreLines(" ")
            .addLoreLines("&fRegistered &c${builder.abilities.size} &fabilities".colorized())
    }
}