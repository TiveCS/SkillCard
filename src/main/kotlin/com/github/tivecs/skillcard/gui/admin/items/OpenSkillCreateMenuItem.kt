package com.github.tivecs.skillcard.gui.admin.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.gui.admin.SkillCreationMenu
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenSkillCreateMenuItem : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        SkillCreationMenu.open(player)
    }

    override fun getItemProvider(): ItemProvider {
        val mat = XMaterial.WRITABLE_BOOK.get() ?: Material.WRITABLE_BOOK

        return ItemBuilder(mat)
            .setDisplayName("&6&lCreate New Skill")
    }
}