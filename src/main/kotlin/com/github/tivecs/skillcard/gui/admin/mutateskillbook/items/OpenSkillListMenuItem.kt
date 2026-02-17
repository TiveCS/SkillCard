package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.entities.skills.Skill
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.SkillListMenu
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

class OpenSkillListMenuItem(
    val displayText: String = "&eOpen Skill List Menu",
    val material: Material = XMaterial.BOOK.get() ?: Material.BOOK,
    val backItem: Item = BorderGuiItem,
    val onSkillItemClick: ((player: Player, skill: Skill) -> Unit) = { _, _ -> },
    val lores : List<String> = emptyList()
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        SkillListMenu.open(
            player = player,
            backItem = backItem,
            onSkillItemClick = onSkillItemClick
        )
    }

    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(material)
            .setDisplayName(displayText.colorized())
            .setLegacyLore(lores.map { it.colorized() })
    }
}