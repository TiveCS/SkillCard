package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.github.tivecs.skillcard.core.entities.books.TriggerTargetSlotSource
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class RegisteredTriggerTargetSlotSourceItem(
    val triggerTargetSlotSource: TriggerTargetSlotSource,
    val onClick: ((player: Player, triggerTargetSlotSource: TriggerTargetSlotSource) -> Unit) = { _, _ -> },
    val displayName: (triggerTargetSlotSource: TriggerTargetSlotSource) -> String = { "&e$it.displayName" },
    val displayLores: (triggerTargetSlotSource: TriggerTargetSlotSource) -> List<String> = { emptyList() },
    val fallbackMaterial: Material = Material.IRON_INGOT,
) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        onClick(player, triggerTargetSlotSource)
    }

    override fun getItemProvider(): ItemProvider? {
        val mat = fallbackMaterial

        return ItemBuilder(mat)
            .setDisplayName(displayName(triggerTargetSlotSource).colorized())
            .setLegacyLore(displayLores(triggerTargetSlotSource).map { it.colorized() })
    }
}