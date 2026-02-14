package com.github.tivecs.skillcard.gui.admin.mutateskill.items

import com.github.tivecs.skillcard.core.builders.skill.SkillAbilityBuilder
import com.github.tivecs.skillcard.core.entities.abilities.Ability
import com.github.tivecs.skillcard.core.entities.skills.SkillAbility
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class ResetEditSkillAttributeItem(
    val displayText: String = "&cReset Edit Skill Attribute",
    val skillAbilityBuilder: SkillAbilityBuilder,
    val skillAbility: SkillAbility? = null,
    val onReset: ((player: Player, skillAbilityBuilder: SkillAbilityBuilder, skillAbility: SkillAbility?) -> Unit) = { _, _, _ -> }
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        if (skillAbility == null) {
            skillAbilityBuilder.reset()
        }else {
            skillAbilityBuilder.reset(skillAbility)
        }
        onReset(player, skillAbilityBuilder, skillAbility)
    }

    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(Material.TNT)
            .setDisplayName("&cReset Edit Skill Attribute".colorized())
            .addLoreLines(" ", "&cClick to reset all changes you made.".colorized())
    }
}