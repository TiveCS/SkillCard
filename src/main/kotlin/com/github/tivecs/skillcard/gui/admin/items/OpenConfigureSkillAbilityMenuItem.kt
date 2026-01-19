package com.github.tivecs.skillcard.gui.admin.items

import com.github.tivecs.skillcard.core.skills.SkillAbility
import com.github.tivecs.skillcard.core.skills.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.ConfigureSkillAbilityAttributeMenu
import com.github.tivecs.skillcard.gui.admin.SkillListedAbilitiesMenu
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenConfigureSkillAbilityMenuItem(
    val skillAbility: SkillAbility,
    val builder: SkillBuilder) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        if (skillAbility.ability == null) {
            throw IllegalStateException("Skill ability is not initialized.")
        }

        ConfigureSkillAbilityAttributeMenu.open(
            viewer = player,
            builder = builder,
            ability = skillAbility.ability,
            onComplete = {
                SkillListedAbilitiesMenu.open(player, builder)
            }
        )
    }
}