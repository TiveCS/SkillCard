package com.github.tivecs.skillcard.gui.admin.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.gui.admin.SkillAdminManageMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenAdminManageMenuItem(val material: XMaterial = XMaterial.BOOKSHELF) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        SkillAdminManageMenu.open(player)
    }

    override fun getItemProvider(): ItemProvider? {
        val mat = material.get() ?: Material.BOOKSHELF

        return ItemBuilder(mat)
            .setDisplayName("&a&lAdmin Manage Menu".colorized())
            .addLoreLines(" ")
            .addLoreLines("&7Click to open the admin manage menu.".colorized())
    }
}