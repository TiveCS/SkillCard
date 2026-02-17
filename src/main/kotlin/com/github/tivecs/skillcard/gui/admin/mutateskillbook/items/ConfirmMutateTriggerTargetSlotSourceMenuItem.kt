package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.book.TriggerTargetSlotSourceBuilder
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.TriggerTargetSlotSourceListMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class ConfirmMutateTriggerTargetSlotSourceMenuItem(
    val targetSlotSourceBuilder: TriggerTargetSlotSourceBuilder,
    val displayText: String = "&bConfirm Create Target Slot Source",
    val lores: List<String> = emptyList(),
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        val validationErrors = targetSlotSourceBuilder.validate()

        if (validationErrors.isNotEmpty()) {
            return
        }

        val skillSetBuilder = targetSlotSourceBuilder.skillSetBuilder
        val targetSlotSource = targetSlotSourceBuilder.build()

        skillSetBuilder.targetSlotSources.add(targetSlotSource)

        TriggerTargetSlotSourceListMenu.open(
            player = player,
            skillSetBuilder = skillSetBuilder,
        )
    }

    override fun getItemProvider(): ItemProvider? {
        val validationErrors = targetSlotSourceBuilder.validate()

        val material =
            if (validationErrors.isEmpty())
                XMaterial.EMERALD.get() ?: Material.EMERALD
            else
                XMaterial.BARRIER.get() ?: Material.BARRIER


        val itemBuilder = ItemBuilder(material)
            .setDisplayName(displayText.colorized())
            .setLegacyLore(lores.map { it.colorized() })

        if (validationErrors.isNotEmpty()) {
            itemBuilder.addLoreLines(" ")
            validationErrors.forEach { error ->
                itemBuilder.addLoreLines("&7- &c$error".colorized())
            }
        }

        return itemBuilder
    }
}