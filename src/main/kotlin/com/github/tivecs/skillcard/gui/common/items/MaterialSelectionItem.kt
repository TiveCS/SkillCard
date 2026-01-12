package com.github.tivecs.skillcard.gui.common.items

import com.cryptomorin.xseries.XMaterial
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.impl.AbstractItem
import java.util.function.Consumer

class MaterialSelectionItem(val material: XMaterial, val onSelect: Consumer<XMaterial>) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        onSelect.accept(material)
    }

}