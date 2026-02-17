package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.github.tivecs.skillcard.core.entities.triggers.Trigger
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class RegisteredTriggerItem(
    val trigger: Trigger<*>,
    val fallbackMaterial: Material = Material.PAPER,
    val onClick: ((player: Player, trigger: Trigger<*>) -> Unit) = { _, _ -> },
    val displayName: (trigger: Trigger<*>) -> String = { it.identifier },
    val displayLores: (trigger: Trigger<*>) -> List<String> = { emptyList() }
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        onClick(player, trigger)
    }

    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(fallbackMaterial)
            .setDisplayName(displayName(trigger).colorized())
            .setLegacyLore(displayLores(trigger).map { it.colorized() })
    }
}