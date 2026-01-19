package com.github.tivecs.skillcard.gui.admin.items

import com.github.tivecs.skillcard.core.skills.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.ConfigureSkillAbilityAttributeMenu
import com.github.tivecs.skillcard.gui.admin.SkillAbilitySelectionMenu
import com.github.tivecs.skillcard.gui.admin.SkillListedAbilitiesMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenAddAbilityToSkillMenuItem(
    val builder: SkillBuilder,
    val displayText: String = "&a&lAdd Ability to Skill") : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        SkillAbilitySelectionMenu.open(
            player,
            builder,
            onSelect = { ability ->
                ConfigureSkillAbilityAttributeMenu.open(
                    viewer = player,
                    builder = builder,
                    ability = ability,
                    onComplete = {
                        SkillListedAbilitiesMenu.open(player, builder)
                    }
                )
            }
        )
    }

    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(Material.FIRE_CHARGE)
            .setDisplayName(displayText.colorized())
            .addLoreLines(" ")
            .addLoreLines("&c${builder.abilities.size} &fabilities are registered".colorized())
    }
}