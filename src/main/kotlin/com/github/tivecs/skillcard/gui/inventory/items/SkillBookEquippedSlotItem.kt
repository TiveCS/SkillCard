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

class SkillBookEquippedSlotItem(val slotData: PlayerInventorySlot) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemProvider(): ItemProvider {
        val book = slotData.book!!
        val material = XMaterial.matchXMaterial(book.material).get().get() ?: Material.BOOK
        val description = book.description.split("\n").map { it.colorized() }.toTypedArray()

        return ItemBuilder(material)
            .setDisplayName(book.displayName.colorized())
            .addLoreLines(*description)
    }
}