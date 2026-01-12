package com.github.tivecs.skillcard.gui.common

import org.bukkit.entity.Player
import xyz.xenondevs.invui.window.AnvilWindow
import java.util.function.Consumer

object InputStringMenu {

    fun open(viewer: Player, title: String, onRename: Consumer<String>): AnvilWindow {
        val window = AnvilWindow.single()
            .setViewer(viewer)
            .setTitle(title)
            .addRenameHandler(onRename)
            .build()

        window.open()

        return window
    }

}