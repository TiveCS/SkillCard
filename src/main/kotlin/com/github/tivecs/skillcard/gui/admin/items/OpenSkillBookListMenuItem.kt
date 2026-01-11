package com.github.tivecs.skillcard.gui.admin.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.gui.admin.SkillBookListMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenSkillBookListMenuItem : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        SkillBookListMenu.open(player)
    }

    override fun getItemProvider(): ItemProvider? {
        val material = XMaterial.ENCHANTED_BOOK.get() ?: Material.ENCHANTED_BOOK
        val skillBookCount = 0

        return ItemBuilder(material)
            .setDisplayName("&a&lSkill Book List".colorized())
            .addLoreLines(" ")
            .addLoreLines("&7There are &e$skillBookCount &7book(s)".colorized())
    }
}