package com.github.tivecs.skillcard.gui.common

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.gui.common.items.MaterialSelectionItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window
import java.util.function.Consumer

object MaterialSelectionMenu {
    fun open(viewer: Player, title: String, onSelect: Consumer<XMaterial>, searchText: String = "") {
        var currentSearchText = searchText
        val itemList = XMaterial.entries
            .filter { mat ->
                val isIncludeInSearch =
                    if (currentSearchText.isBlank()) true
                    else mat.name.contains(currentSearchText.trim(), ignoreCase = true)

                mat.isSupported && isIncludeInSearch
            }
            .map { material ->
                MaterialSelectionItem(material, onSelect)
            }

        val gui = PagedGui.items()
            .setStructure(
                "xxxxxxxx#",
                "xxxxxxxx<",
                "xxxxxxxxS",
                "xxxxxxxx>",
                "xxxxxxxx#",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient(
                'S', OpenInputStringMenuItem(
                    displayName = "&e&lSearch Material",
                    title = "Search Material",
                    initialValue = currentSearchText,
                    onRename = { text ->
                        currentSearchText = text
                    },
                    onConfirm = { _, _, _ ->
                        open(viewer, title, onSelect, currentSearchText)
                    },
                )
            )
            .setContent(itemList)
            .build()

        val window = Window.single()
            .setGui(gui)
            .setTitle(title.colorized())
            .setViewer(viewer)
            .build()

        window.open()
    }

}