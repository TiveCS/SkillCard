package com.github.tivecs.skillcard.gui.common.items

import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.controlitem.ScrollItem

class ScrollDownItem : ScrollItem(-1) {
    override fun getItemProvider(gui: ScrollGui<*>): ItemProvider? {
        val builder = ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)

        builder.setDisplayName("Scroll down")

        if (!gui.canScroll(1))
            builder.addLoreLines("You can't scroll further down")

        return builder
    }
}