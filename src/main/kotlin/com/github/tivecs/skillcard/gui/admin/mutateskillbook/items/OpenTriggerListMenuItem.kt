package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.entities.triggers.Trigger
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.TriggerListMenu
import com.github.tivecs.skillcard.gui.common.items.BorderGuiItem
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenTriggerListMenuItem(
    val material: Material = XMaterial.STONE_SWORD.get() ?: Material.STONE_SWORD,
    val displayText: String = "&aOpen Trigger List Menu",
    val lores: List<String> = listOf(" ", "&fClick to open the Trigger List Menu"),
    val displayTriggerItemLores: (trigger: Trigger<*>) -> List<String> = { emptyList() },
    val displayTriggerText: (trigger: Trigger<*>) -> String = { trigger -> trigger.identifier },
    val backItem: Item = BorderGuiItem,
    val triggerPredicate: (trigger: Trigger<*>) -> Boolean = { true },
    val onMenuItemClick: (player: Player, trigger: Trigger<*>) -> Unit = {_, _ -> },
) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        TriggerListMenu.open(
            player = player,
            backItem = backItem,
            onClick = onMenuItemClick,
            predicate = triggerPredicate,
            triggerItemLores = displayTriggerItemLores,
            displayName = displayTriggerText
        )
    }


    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(material)
            .setDisplayName(displayText.colorized())
            .setLegacyLore(lores.map { it.colorized() })
    }
}