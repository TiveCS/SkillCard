package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.book.SkillBookBuilder
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

class OpenMutateSkillBookSkillSetMenuItem(
    val material: Material = XMaterial.BOOKSHELF.get() ?: Material.BOOKSHELF,
    val displayText: String = "&aCreate New Skill Set",
    val bookBuilder: SkillBookBuilder,
    val lores: List<String> = listOf(" ", "&fRegistered &b${bookBuilder.triggerSkillSets.size}&f skill sets")
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        MutateSkillBookSkillSetMenu.open(player, bookBuilder)
    }

    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(material)
            .setDisplayName(displayText.colorized())
            .setLegacyLore(lores.map { it.colorized() })
    }
}