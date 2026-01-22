package com.github.tivecs.skillcard.gui.admin.mutateskill.items

import com.github.tivecs.skillcard.core.builders.skill.SkillAbilityBuilder
import com.github.tivecs.skillcard.core.entities.abilities.AbilityRequirement
import com.github.tivecs.skillcard.gui.common.InputStringMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.util.Consumer
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

@Suppress("UNCHECKED_CAST")
class EditSkillAbilityAttributeValueItem<TValue : Any>(
    val abilityBuilder: SkillAbilityBuilder,
    val requirement: AbilityRequirement,
    val initialValue: TValue? = null,
    val onValueChange: Consumer<TValue> = { },
    val onConfirm: () -> Unit = { }
) : AbstractItem() {

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        when (requirement.targetType) {
            Int::class, Long::class, Short::class, Byte::class -> {
                InputStringMenu.open(
                    viewer = player,
                    title = "",
                    initialValue = initialValue as? String ?: "",
                    onRename = { input ->
                        when (requirement.targetType) {
                            Int::class -> {
                                onValueChange.accept(input.toInt() as TValue?)
                            }

                            Long::class -> {
                                onValueChange.accept(input.toLong() as TValue?)
                            }

                            Short::class -> {
                                onValueChange.accept(input.toShort() as TValue?)
                            }

                            Byte::class -> {
                                onValueChange.accept(input.toByte() as TValue?)
                            }
                        }
                    },
                    onConfirm = { _, _, _ -> onConfirm.invoke() }
                )
            }

            Double::class, Float::class -> {
                InputStringMenu.open(
                    viewer = player,
                    title = "",
                    initialValue = initialValue as? String ?: "",
                    onRename = { input ->
                        when (requirement.targetType) {
                            Double::class -> {
                                onValueChange.accept(input.toDouble() as TValue?)
                            }

                            Float::class -> {
                                onValueChange.accept(input.toFloat() as TValue?)
                            }
                        }
                    },
                    onConfirm = { _, _, _ -> onConfirm.invoke() }
                )
            }

            String::class -> {
                InputStringMenu.open(
                    viewer = player,
                    title = "",
                    initialValue = initialValue as? String ?: "",
                    onRename = { input -> onValueChange.accept(input as TValue?) },
                    onConfirm = { _, _, _ -> onConfirm.invoke() }
                )
            }

            else -> throw UnsupportedOperationException("Unsupported target type ${requirement.targetType}")
        }
    }

    override fun getItemProvider(): ItemProvider {
        val result = ItemBuilder(Material.BRICK).setDisplayName("&e${requirement.key}")

        if (!requirement.description.isNullOrBlank()) {
            result.addLoreLines(" ", requirement.description.colorized())
        }

        result.addLoreLines(" ", "&fCurrent Value: ${abilityBuilder.attributes.getOrDefault(requirement.key, "-")}")

        return result
    }

}