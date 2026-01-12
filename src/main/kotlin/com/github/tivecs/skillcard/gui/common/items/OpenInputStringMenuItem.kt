package com.github.tivecs.skillcard.gui.common.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.gui.common.InputStringMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem
import java.util.function.Consumer

class OpenInputStringMenuItem(
    val displayName: String = "&e&lInput string",
    val material: Material = XMaterial.NAME_TAG.get() ?: Material.NAME_TAG,
    val title: String,
    val onRename: Consumer<String>,
    val lores: List<String> = emptyList()) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        InputStringMenu.open(player, title, onRename)
    }

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(material)
            .setDisplayName(displayName.colorized())
            .setLegacyLore(lores.map { lore -> lore.colorized() })
    }
}