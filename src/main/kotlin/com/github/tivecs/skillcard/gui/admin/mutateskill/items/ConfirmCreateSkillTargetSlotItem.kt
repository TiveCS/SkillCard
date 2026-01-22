package com.github.tivecs.skillcard.gui.admin.mutateskill.items

import com.github.tivecs.skillcard.core.builders.skill.SkillTargetSlotBuilder
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class ConfirmCreateSkillTargetSlotItem(
    val builder: SkillTargetSlotBuilder,
    val onPostCreate: () -> Unit = { }
) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        val errors = builder.validate()

        if (errors.isNotEmpty()) return

        val targetSlot = builder.build()

        builder.skillBuilder.targetSlots.add(targetSlot)
        builder.skillBuilder.targetSlotBuilder = null

        onPostCreate.invoke()
    }

    override fun getItemProvider(): ItemProvider {
        val errors = builder.validate()
        val isValid = errors.isEmpty()

        val result = ItemBuilder(if (isValid) Material.EMERALD else Material.BARRIER)
            .setDisplayName("&6Confirm Create Skill Target Slot".colorized())

        if (!isValid) {
            result.addLoreLines(" ")
            errors.forEach { error ->
                result.addLoreLines("&7- &c${error}".colorized())
            }
        }

        return result
    }

}