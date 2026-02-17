package com.github.tivecs.skillcard.gui.admin.common

import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class SelectionItem<TValue>(
    val value: TValue,
    val displayName: (TValue) -> String = { it.toString() },
    val displayLores: (TValue) -> List<String> = { emptyList() },
    val material: (TValue) -> Material = { Material.PAPER },
    val fallbackMaterial: Material = Material.PAPER,
    val onClick: (Player, TValue) -> Unit = { _, _ -> },
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        onClick(player, value)
    }

    override fun getItemProvider(): ItemProvider? {
        val mat = material(value) ?: fallbackMaterial

        return ItemBuilder(mat)
            .setDisplayName(displayName(value).colorized())
            .setLegacyLore(displayLores(value).map { it.colorized() })
    }
}