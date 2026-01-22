package com.github.tivecs.skillcard.gui.admin.mutateskill.items

import com.github.tivecs.skillcard.core.builders.skill.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.mutateskill.SkillTargetSlotListMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenSkillTargetSlotListMenuItem(
    val displayText: String = "&eSkill Target Slot List",
    val skillBuilder: SkillBuilder
) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        SkillTargetSlotListMenu.open(player, skillBuilder)
    }

    override fun getItemProvider(): ItemProvider {
        val result = ItemBuilder(Material.TARGET).setDisplayName(displayText.colorized())
            .addLoreLines(" ", "&fThere are &b${skillBuilder.targetSlots.size} &fregistered target slots".colorized())

        if (skillBuilder.targetSlotBuilder != null) {
            result.setDisplayName(displayText.colorized() + " &8[&eDRAFT&8]".colorized())
        }

        return result
    }

}