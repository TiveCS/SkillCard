package com.github.tivecs.skillcard.gui.common.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.gui.common.MaterialSelectionMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem
import java.util.function.Consumer

class OpenSelectMaterialMenuItem(val title: String, val onSelect: Consumer<XMaterial>) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        MaterialSelectionMenu.open(player, title, onSelect)
    }

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(Material.GRASS_BLOCK)
            .setDisplayName(title.colorized())
    }
}