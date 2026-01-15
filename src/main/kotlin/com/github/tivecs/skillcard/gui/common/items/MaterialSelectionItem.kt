package com.github.tivecs.skillcard.gui.common.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem
import java.util.function.Consumer

class MaterialSelectionItem(val material: XMaterial, val onSelect: Consumer<XMaterial>) : AbstractItem() {

    private var isUnsupported = false

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        if (isUnsupported) {
            player.sendMessage("&cThis material is not supported in your current version.".colorized())
            return
        }

        onSelect.accept(material)
    }

    override fun getItemProvider(): ItemProvider? {
        val mat = when (val getMat = material.get()) {
            null -> {
                isUnsupported = true
                Material.BARRIER
            }
            else -> getMat
        }

        val builder = ItemBuilder(mat)

        if (isUnsupported) {
            builder
                .setDisplayName("&c${material.friendlyName()} (Unsupported)".colorized())
                .addLoreLines(" ", "&cNot supported in current server version.".colorized())
        }else {
            builder.addLoreLines(" ", "&aClick to Select".colorized())
        }

        return builder
    }

}