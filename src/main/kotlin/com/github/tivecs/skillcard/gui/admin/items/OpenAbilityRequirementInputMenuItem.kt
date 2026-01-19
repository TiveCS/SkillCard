package com.github.tivecs.skillcard.gui.admin.items

import com.github.tivecs.skillcard.core.abilities.AbilityRequirement
import com.github.tivecs.skillcard.core.skills.SkillAbilityBuilder
import com.github.tivecs.skillcard.core.skills.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.ConfigureSkillAbilityAttributeMenu
import com.github.tivecs.skillcard.gui.admin.SkillListedAbilitiesMenu
import com.github.tivecs.skillcard.gui.common.InputNumberMenu
import com.github.tivecs.skillcard.gui.common.InputStringMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenAbilityRequirementInputMenuItem(
    val requirement: AbilityRequirement,
    val skillBuilder: SkillBuilder,
    val abilityBuilder: SkillAbilityBuilder
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        if (!requirement.choices.isNullOrEmpty()) {
            TODO("open selection menu for choices")
            return
        }

        when (requirement.targetType) {
            Int::class, Short::class,
            Long::class, Byte::class -> {
                InputNumberMenu.open(
                    viewer = player,
                    initialValue = 0,
                    onRename = { num ->
                        abilityBuilder.addAttribute(requirement.key, num)
                    },
                    onConfirm = { _, viewer, _ ->
                        ConfigureSkillAbilityAttributeMenu.open(
                            viewer = viewer,
                            builder = skillBuilder,
                            ability = abilityBuilder.ability,
                            onComplete = {
                                SkillListedAbilitiesMenu.open(player, skillBuilder)
                            }
                        )
                    }
                )
            }
            Double::class, Float::class -> {
                InputNumberMenu.open(
                    viewer = player,
                    initialValue = 0.0,
                    onRename = { num ->
                        abilityBuilder.addAttribute(requirement.key, num)
                    },
                    onConfirm = { _, viewer, _ ->
                        ConfigureSkillAbilityAttributeMenu.open(
                            viewer = viewer,
                            builder = skillBuilder,
                            ability = abilityBuilder.ability,
                            onComplete = {
                                SkillListedAbilitiesMenu.open(player, skillBuilder)
                            }
                        )
                    }
                )
            }
            String::class -> {
                InputStringMenu.open(
                    viewer = player,
                    initialValue = "",
                    onRename = { str ->
                        abilityBuilder.addAttribute(requirement.key, str)
                    },
                    onConfirm = { _, viewer, _ ->
                        ConfigureSkillAbilityAttributeMenu.open(
                            viewer = viewer,
                            builder = skillBuilder,
                            ability = abilityBuilder.ability,
                            onComplete = {
                                SkillListedAbilitiesMenu.open(player, skillBuilder)
                            }
                        )
                    }
                )
            }
            else -> throw IllegalArgumentException("Unsupported requirement target type: ${requirement.targetType}")
        }
    }

    override fun getItemProvider(): ItemProvider? {
        val numericValue: Int = when (requirement.targetType) {
            Int::class, Short::class,
            Long::class, Byte::class -> abilityBuilder.getAttribute(requirement.key) as? Int ?: 1
            Double::class, Float::class -> abilityBuilder.getAttribute(requirement.key) as? Int ?: 1
            else -> 1
        }

        return ItemBuilder(Material.BOOK)
            .setAmount(numericValue)
            .setDisplayName("&a${requirement.key}".colorized())
            .addLoreLines(" ")
            .addLoreLines("&fCurrent: &a${abilityBuilder.getAttribute(requirement.key) ?: "&cNot Set"}".colorized())
    }
}