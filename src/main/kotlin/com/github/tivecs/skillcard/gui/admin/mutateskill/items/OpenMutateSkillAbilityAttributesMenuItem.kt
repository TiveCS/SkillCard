package com.github.tivecs.skillcard.gui.admin.mutateskill.items

import com.github.tivecs.skillcard.core.builders.skill.SkillAbilityBuilder
import com.github.tivecs.skillcard.gui.admin.mutateskill.MutateSkillAbilityAttributesMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenMutateSkillAbilityAttributesMenuItem(
    val displayText: String = "&cEdit Skill Ability Attributes",
    val abilityBuilder: SkillAbilityBuilder
) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        MutateSkillAbilityAttributesMenu.open(player, abilityBuilder)
    }

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(Material.RED_BANNER).setDisplayName(displayText.colorized())
    }
}