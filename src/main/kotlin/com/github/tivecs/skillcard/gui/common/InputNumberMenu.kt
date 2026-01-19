@file:Suppress("UNCHECKED_CAST")

package com.github.tivecs.skillcard.gui.common

import com.github.tivecs.skillcard.gui.common.items.ConfirmInputStringItem
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.AnvilWindow
import java.util.function.Consumer

object InputNumberMenu {
    fun <TValue : Number> open(
        viewer: Player,
        title: String = "",
        initialValue: TValue,
        onRename: Consumer<TValue>,
        onConfirm: (clickType: ClickType, player: Player, event: InventoryClickEvent) -> Unit = { _, _, _ -> }
    ): AnvilWindow {
        val upperGui = Gui.normal()
            .setStructure(3, 1, "#NC")
            .addIngredient('#', SimpleItem(ItemBuilder(Material.PAPER).setDisplayName(initialValue.toString())))
            .addIngredient('N', SimpleItem(ItemBuilder(Material.NAME_TAG).setDisplayName("&e&lEnter Number Value Above".colorized())))
            .addIngredient('C', ConfirmInputStringItem(onConfirm))
            .build()

        val window = AnvilWindow.single()
            .setViewer(viewer)
            .setGui(upperGui)
            .setTitle(title)
            .addRenameHandler({ newValueStr ->
                val newValue: TValue = when (initialValue) {
                    is Int -> newValueStr.toIntOrNull() as TValue? ?: initialValue
                    is Double -> newValueStr.toDoubleOrNull() as TValue? ?: initialValue
                    is Float -> newValueStr.toFloatOrNull() as TValue? ?: initialValue
                    is Long -> newValueStr.toLongOrNull() as TValue? ?: initialValue
                    is Short -> newValueStr.toShortOrNull() as TValue? ?: initialValue
                    is Byte -> newValueStr.toByteOrNull() as TValue? ?: initialValue
                    else -> initialValue
                }

                onRename.accept(newValue)
            })
            .build()

        window.open()

        return window
    }
}