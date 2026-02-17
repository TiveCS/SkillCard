package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.book.SkillBookBuilder
import com.github.tivecs.skillcard.core.entities.books.TriggerSkillSet
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.SkillBookSkillSetListMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenSkillBookSkillSetMenuItem(
    val displayText: String = "&eOpen Skill Set List Menu",
    val onSkillSetItemClick: ((player: Player, skillSet: TriggerSkillSet) -> Unit) = { _, _ -> },
    val skillBookBuilder: SkillBookBuilder,
    val lores: List<String> = listOf(" ", "&fRegistered &b${skillBookBuilder.triggerSkillSets.size}&f skill sets"),
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        SkillBookSkillSetListMenu.open(
            player = player,
            skillBookBuilder = skillBookBuilder,
            onSkillSetClick = onSkillSetItemClick
        )
    }

    override fun getItemProvider(): ItemProvider? {
        val mat = XMaterial.ENCHANTING_TABLE.get() ?: Material.ENCHANTING_TABLE

        return ItemBuilder(mat)
            .setDisplayName(displayText.colorized())
            .setLegacyLore(lores.map { it.colorized() })
    }
}