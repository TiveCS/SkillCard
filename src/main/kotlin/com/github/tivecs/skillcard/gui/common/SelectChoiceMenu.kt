package com.github.tivecs.skillcard.gui.common

import com.github.tivecs.skillcard.gui.common.items.SelectOptionItem
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window

object SelectChoiceMenu {

    fun <TValue> open(viewer: Player, choices: List<SelectOptionItem<TValue>>) {

        val gui = PagedGui.items()
            .setStructure(
                "#########",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "#########",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .setContent(choices)
            .build()

        val window = Window.single()
            .setGui(gui)
            .setTitle("Make a Choice")
            .setViewer(viewer)
            .build()

        window.open()
    }

}