package com.github.tivecs.skillcard.gui.admin.mutateskill.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.skill.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.mutateskill.MutateSkillTargetSlotMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenMutateSkillTargetSlotMenuItem(
    val builder: SkillBuilder,
    val displayText: String = "&aNew Target Slot") : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        MutateSkillTargetSlotMenu.open(player, builder)
    }

    override fun getItemProvider(): ItemProvider? {
        val material = XMaterial.GUNPOWDER.get() ?: Material.GUNPOWDER;

        return ItemBuilder(material).setDisplayName(displayText.colorized())
    }
}