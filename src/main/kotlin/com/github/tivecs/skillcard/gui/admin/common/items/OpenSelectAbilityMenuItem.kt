package com.github.tivecs.skillcard.gui.admin.common.items

import com.github.tivecs.skillcard.core.entities.abilities.Ability
import com.github.tivecs.skillcard.gui.admin.common.SelectAbilityMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem
import java.util.function.Consumer

class OpenSelectAbilityMenuItem(
    val displayText: String = "&cSelect Ability",
    val onSelect: Consumer<Ability<*>>,
    val backItem: AbstractItem? = null
) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        SelectAbilityMenu.open(player, onSelect, backItem = backItem)
    }

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(Material.BLAZE_POWDER).setDisplayName(displayText.colorized())
    }

}