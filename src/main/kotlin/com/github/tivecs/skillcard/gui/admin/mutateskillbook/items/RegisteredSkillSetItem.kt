package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.github.tivecs.skillcard.core.entities.books.TriggerSkillSet
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class RegisteredSkillSetItem(
    val skillSet: TriggerSkillSet,
    val onClick: ((player: Player, skillSet: TriggerSkillSet) -> Unit) = { _, _ -> },
    val displayName: (skillSet: TriggerSkillSet) -> String = { "&e$it.displayName" },
    val displayLores: (skillSet: TriggerSkillSet) -> List<String> = { emptyList() },
    val fallbackMaterial: Material = Material.SLIME_BALL,
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        onClick(player, skillSet)
    }

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(fallbackMaterial)
            .setDisplayName(displayName(skillSet).colorized())
            .setLegacyLore(displayLores(skillSet).map { it.colorized() })
    }
}