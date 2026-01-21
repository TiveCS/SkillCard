package com.github.tivecs.skillcard.gui.admin.items

import com.github.tivecs.skillcard.core.skills.SkillBuilder
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenSkillTriggerTargetSlotMenuItem(val builder: SkillBuilder) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(Material.PRISMARINE_SHARD)
            .setDisplayName("&3&lEdit Trigger Target Slot".colorized())
            .addLoreLines(" ")
    }
}