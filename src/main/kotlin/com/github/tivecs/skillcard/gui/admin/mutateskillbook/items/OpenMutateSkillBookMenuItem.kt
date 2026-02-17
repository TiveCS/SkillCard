package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.MutateSkillBookMenu
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.MutateSkillBookSkillSetMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenMutateSkillBookMenuItem(
    val material: Material = XMaterial.KNOWLEDGE_BOOK.get() ?: Material.KNOWLEDGE_BOOK,
    val displayText: String = "&aOpen Skill Book Menu Editor",
    val lores: List<String> = listOf(" ", "&fClick to open the Skill Book Menu Editor"),
    val onClick: ((player: Player) -> Unit) = { player ->
        MutateSkillBookMenu.open(player)
    }
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        onClick(player)
    }

    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(material)
            .setDisplayName(displayText.colorized())
            .setLegacyLore(lores.map { it.colorized() })
    }
}