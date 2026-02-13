package com.github.tivecs.skillcard.gui.common.items

import org.bukkit.Material
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.controlitem.ScrollItem

class ScrollUpItem : ScrollItem(-1) {

    override fun getItemProvider(gui: ScrollGui<*>): ItemProvider {
        val builder = ItemBuilder(Material.RED_STAINED_GLASS_PANE)
        builder.setDisplayName("Scroll up")
        if (!gui.canScroll(-1))
            builder.addLoreLines("You've reached the top")
        return builder
    }

}