package com.github.tivecs.skillcard.gui.admin.mutateskill.items

import com.github.tivecs.skillcard.gui.admin.mutateskill.MutateSkillMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenMutateSkillMenuItem(val displayText: String = "&aCreate New Skill") : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        MutateSkillMenu.open(player)
    }

    override fun getItemProvider(): ItemProvider {
        val result = ItemBuilder(Material.WRITABLE_BOOK)
            .setDisplayName(displayText.colorized())

        return result
    }
}