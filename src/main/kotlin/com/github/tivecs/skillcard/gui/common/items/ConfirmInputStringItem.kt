package com.github.tivecs.skillcard.gui.common.items

import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class ConfirmInputStringItem(
    val onConfirm: (clickType: ClickType, player: Player, event: InventoryClickEvent) -> Unit = { _, _, _ -> }
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        onConfirm?.invoke(clickType, player, event)
    }

    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(Material.PAPER).setDisplayName("&a&lClick to Confirm".colorized())
    }
}