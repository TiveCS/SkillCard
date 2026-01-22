package com.github.tivecs.skillcard.gui.admin.common.items

import com.github.tivecs.skillcard.core.entities.abilities.Ability
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.impl.AbstractItem
import java.util.function.Consumer

class AbilitySelectionOptionItem(val ability: Ability<*>, val onSelect: Consumer<Ability<*>>) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemProvider(): ItemProvider {
        return super.getItemProvider()
    }
}