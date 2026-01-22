package com.github.tivecs.skillcard.gui.admin.common

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.entities.abilities.Ability
import com.github.tivecs.skillcard.gui.common.items.BorderGuiItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.impl.AbstractItem
import xyz.xenondevs.invui.window.Window
import java.util.function.Consumer

object SelectAbilityMenu {

    fun open(viewer: Player, onSelect: Consumer<Ability<*>>, searchText: String = "", backItem: AbstractItem? = null) {
        var search = searchText

        val gui = ScrollGui.items()
            .setStructure(
                "xxxxxxxxB",
                "xxxxxxxx<",
                "xxxxxxxxS",
                "xxxxxxxx>",
                "xxxxxxxx#",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_VERTICAL)
            .addIngredient('B', backItem ?: BorderGuiItem)
            .addIngredient(
                'S', OpenInputStringMenuItem(
                    displayName = "&aSearch Abilities",
                    material = XMaterial.COMPASS.get() ?: Material.COMPASS,
                    title = "Search Abilities",
                    initialValue = search,
                    onRename = { input -> search = input },
                    onConfirm = { _, _, _ -> open(viewer, onSelect, search) },
                )
            )
            .build()

        val window = Window.single().setGui(gui).setViewer(viewer).build()

        window.open()
    }

}