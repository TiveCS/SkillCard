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

object InputStringMenu {

    fun open(
        viewer: Player,
        title: String = "",
        initialValue: String,
        onRename: Consumer<String>,
        onConfirm: (clickType: ClickType, player: Player, event: InventoryClickEvent) -> Unit = { _, _, _ -> }
    ): AnvilWindow {
        val upperGui = Gui.normal()
            .setStructure(3, 1, "#NC")
            .addIngredient('#', SimpleItem(ItemBuilder(Material.PAPER).setDisplayName(initialValue.colorized())))
            .addIngredient('N', SimpleItem(ItemBuilder(Material.NAME_TAG).setDisplayName("&e&lEnter Text Above".colorized())))
            .addIngredient('C', ConfirmInputStringItem(onConfirm))
            .build()

        val window = AnvilWindow.single()
            .setViewer(viewer)
            .setTitle(title)
            .setGui(upperGui)
            .addRenameHandler(onRename)
            .build()

        window.open()

        return window
    }

}