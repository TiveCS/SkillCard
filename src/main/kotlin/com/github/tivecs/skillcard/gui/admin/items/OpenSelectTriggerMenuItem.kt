package com.github.tivecs.skillcard.gui.admin.items

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

class OpenSelectTriggerMenuItem(val onSelect: Consumer<String>) : AbstractItem() {
    val availableTriggerItems: ArrayList<AbstractItem>

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemProvider(): ItemProvider? {
        val mat = XMaterial.TARGET.get() ?: Material.TARGET

        return ItemBuilder(mat)
            .setDisplayName("&cAdd Trigger Variation".colorized())
    }

    init {
        // TODO: get available triggers and map it into clickable items
        availableTriggerItems = arrayListOf()
    }
}