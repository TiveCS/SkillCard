package com.github.tivecs.skillcard.gui.admin.mutateskill.items

import com.github.tivecs.skillcard.core.builders.skill.SkillAbilityBuilder
import com.github.tivecs.skillcard.gui.admin.mutateskill.SkillAbilityListMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class ConfirmAddSkillAbilityItem(val abilityBuilder: SkillAbilityBuilder) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        val errors = abilityBuilder.validate()

        if (errors.isNotEmpty()) return

        val skillBuilder = abilityBuilder.skillBuilder

        val skillAbility = abilityBuilder.build()
        skillBuilder.abilities.add(skillAbility)
        skillBuilder.abilityBuilder = null

        SkillAbilityListMenu.open(player, skillBuilder)
    }

    override fun getItemProvider(): ItemProvider {
        val errors = abilityBuilder.validate()
        val isValid = errors.isEmpty()
        val mat = if (isValid) Material.EMERALD else Material.BARRIER

        val result = ItemBuilder(mat).setDisplayName("&aSave Skill Ability".colorized())

        if (!isValid) {
            result.addLoreLines(" ")
            errors.forEach { error ->
                result.addLoreLines("&7- &c$error".colorized())
            }
        }

        return result
    }

}