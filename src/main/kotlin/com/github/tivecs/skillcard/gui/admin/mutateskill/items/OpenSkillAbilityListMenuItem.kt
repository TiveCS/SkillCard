package com.github.tivecs.skillcard.gui.admin.mutateskill.items

import com.github.tivecs.skillcard.core.builders.skill.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.mutateskill.SkillAbilityListMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenSkillAbilityListMenuItem(
    val displayText: String = "&aOpen Skill Ability List Menu",
    val skillBuilder: SkillBuilder) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        SkillAbilityListMenu.open(player, skillBuilder)
    }

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(Material.BLAZE_ROD).setDisplayName(displayText.colorized())
    }
}