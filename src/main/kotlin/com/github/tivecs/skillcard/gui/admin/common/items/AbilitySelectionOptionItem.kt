package com.github.tivecs.skillcard.gui.admin.common.items

import com.github.tivecs.skillcard.core.entities.abilities.Ability
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.builder.setDisplayName
import xyz.xenondevs.invui.item.impl.AbstractItem
import java.util.function.Consumer

class AbilitySelectionOptionItem(
    val ability: Ability<*>,
    val onSelect: Consumer<Ability<*>>) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        onSelect.accept(ability)
    }

    override fun getItemProvider(): ItemProvider {
        val descriptionList = ability.description.split("\n").map {
            it.trim().colorized()
        }.toMutableList()

        descriptionList.add(0, " ")

        return ItemBuilder(ability.material)
            .setDisplayName("&b${ability.displayName}".colorized())
            .setLegacyLore(descriptionList)
    }
}