package com.github.tivecs.skillcard.gui.inventory.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.player.PlayerInventorySlot
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class SkillBookFillableSlotItem(val slotData: PlayerInventorySlot) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(XMaterial.WHITE_STAINED_GLASS_PANE.get() ?: Material.WHITE_STAINED_GLASS_PANE)
            .setDisplayName("&eSkill Book Slot #&6${slotData.slotIndex}".colorized())
            .addLoreLines("&7Place your skill book here to equip".colorized())
    }

}