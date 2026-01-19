package com.github.tivecs.skillcard.gui.admin.items

import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem
import java.util.function.Consumer

class AbilitySelectItem(val ability: Ability<*>, val onSelect: Consumer<Ability<*>>) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        onSelect.accept(ability)
    }

    override fun getItemProvider(): ItemProvider? {
        val descriptionList = ability.description.split("\n").map { it.trim().colorized() }.toTypedArray()

        return ItemBuilder(ability.material)
            .setDisplayName(ability.displayName.colorized())
            .addLoreLines(" ")
            .addLoreLines(*descriptionList)
            .addLoreLines(" ", "&aClick to Select".colorized())
    }
}