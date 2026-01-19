package com.github.tivecs.skillcard.gui.common.items

import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

class AbilityDisplayItem(
    val ability: Ability<*>,
    val materialDisplay: Material = Material.PAPER
) : SimpleItem(
    ItemBuilder(materialDisplay)
        .setDisplayName(ability.displayName.colorized())
        .addLoreLines(" ")
        .addLoreLines(*ability.description.split("\n").map { it.trim().colorized() }.toTypedArray())
)