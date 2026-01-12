package com.github.tivecs.skillcard.gui.common

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.gui.common.items.MaterialSelectionItem
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window
import java.util.function.Consumer

object MaterialSelectionMenu {
    fun open(viewer: Player, title: String, onSelect: Consumer<XMaterial>) {
        val itemList = XMaterial.entries.map { material ->
            MaterialSelectionItem(material, onSelect)
        }

        val gui = PagedGui.items()
            .setStructure(
                "xxxxxxxx#",
                "xxxxxxxx#",
                "xxxxxxxx>",
                "xxxxxxxx<",
                "xxxxxxxx#",
                "xxxxxxxx#",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .setContent(itemList)
            .build()

        val window = Window.single()
            .setGui(gui)
            .setViewer(viewer)
            .build()

        window.open()
    }

}