package com.github.tivecs.skillcard.gui.admin.items

import com.github.tivecs.skillcard.core.skills.SkillAbilityBuilder
import com.github.tivecs.skillcard.core.skills.SkillBuilder
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class ConfirmConfigureAbilityItem(
    val skillBuilder: SkillBuilder,
    val abilityBuilder: SkillAbilityBuilder,
    val onComplete: () -> Unit
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        val errors = abilityBuilder.validate()
        if (errors.isNotEmpty()) {
            player.sendMessage("&cCannot confirm ability configuration due to the following errors:".colorized())
            errors.forEach { error ->
                player.sendMessage("&8- &c${error}".colorized())
            }
            return
        }

        val skillAbility = abilityBuilder.build()
        skillBuilder.addAbilities(skillAbility)
        onComplete.invoke()
    }

    override fun getItemProvider(): ItemProvider? {
        val errors = abilityBuilder.validate()

        val builder = ItemBuilder(if (errors.isNotEmpty()) Material.BARRIER else Material.EMERALD)
            .setDisplayName("&a&lConfirm Ability Configuration".colorized())

        if (errors.isNotEmpty()) {
            builder.addLoreLines(" ", "&eValidation Errors:".colorized())
            errors.forEach { error ->
                builder.addLoreLines("&8- &c${error}".colorized())
            }
        }

        return builder
    }
}