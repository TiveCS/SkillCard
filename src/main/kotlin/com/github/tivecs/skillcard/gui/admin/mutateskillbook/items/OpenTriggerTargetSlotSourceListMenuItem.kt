package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.book.TriggerSkillSetBuilder
import com.github.tivecs.skillcard.core.entities.books.TriggerTargetSlotSource
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.TriggerTargetSlotSourceListMenu
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

class OpenTriggerTargetSlotSourceListMenuItem(
    val skillSetBuilder: TriggerSkillSetBuilder,
    val material: Material = XMaterial.STICK.get() ?: Material.STICK,
    val displayText: String = "&bOpen Trigger Target Slot Source List Menu",
    val lores: List<String> = emptyList(),
    val backItem: Item = OpenMutateSkillBookSkillSetMenuItem(
        displayText = "&eBack to Skill Set Editor",
        bookBuilder = skillSetBuilder.skillBookBuilder,
    ),
    val onTargetSlotSourceClick: (Player, TriggerTargetSlotSource) -> Unit = { _, _ -> },
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        TriggerTargetSlotSourceListMenu.open(
            player = player,
            skillSetBuilder = skillSetBuilder,
            backItem = backItem,
            onTargetSlotSourceClick = onTargetSlotSourceClick
        )
    }

    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(material)
            .setDisplayName(displayText.colorized())
            .setLegacyLore(lores.map { it.colorized() })
    }
}