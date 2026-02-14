package com.github.tivecs.skillcard.gui.admin.mutateskill.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.skill.SkillBuilder
import com.github.tivecs.skillcard.core.entities.skills.SkillTargetSlot
import com.github.tivecs.skillcard.gui.admin.mutateskill.SkillTargetSlotListMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class SkillTargetSlotMenuItem(
    val material: XMaterial = XMaterial.REDSTONE,
    val targetSlot: SkillTargetSlot,
    val skillBuilder: SkillBuilder,
    val lores: List<String> = listOf(),
    val onClick: ((player: Player, skillBuilder: SkillBuilder, targetSlot: SkillTargetSlot) -> Unit) = { _, _, _ -> }
) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        onClick(player, skillBuilder, targetSlot)
    }

    override fun getItemProvider(): ItemProvider? {
        val mat = material.get() ?: Material.REDSTONE

        return ItemBuilder(mat)
            .setDisplayName("&a${targetSlot.identifier}".colorized())
            .setLegacyLore(lores)
    }
}