package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.book.SkillBookBuilder
import com.github.tivecs.skillcard.core.builders.book.TriggerSkillSetBuilder
import com.github.tivecs.skillcard.core.entities.books.TriggerTargetSlotSource
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.MutateTriggerTargetSlotSourceMenu
import com.github.tivecs.skillcard.gui.common.items.BorderGuiItem
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenMutateTriggerTargetSlotSourceMenuItem(
    val displayText: String = "&eNew Trigger Target Slot Source",
    val lores: List<String> = listOf(
        " ",
        "&fRegistered &b${skillSetBuilder.targetSlotSources.size}&f trigger target slot sources"
    ),
    val material: Material = XMaterial.FIRE_CHARGE.get() ?: Material.FIRE_CHARGE,
    val skillSetBuilder: TriggerSkillSetBuilder
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        MutateTriggerTargetSlotSourceMenu.open(
            player = player,
            skillSetBuilder = skillSetBuilder,
            backItem = OpenTriggerTargetSlotSourceListMenuItem(
                skillSetBuilder = skillSetBuilder,
                displayText = "&eBack to Target Source List",
            )
        )
    }

    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(material)
            .setDisplayName(displayText.colorized())
            .setLegacyLore(lores.map { it.colorized() })
    }
}