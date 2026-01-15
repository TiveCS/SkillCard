package com.github.tivecs.skillcard.gui.common.items

import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.controlitem.PageItem

class PreviousPageGuiItem : PageItem(false) {

    override fun getItemProvider(gui: PagedGui<*>): ItemProvider {
        val builder = ItemBuilder(Material.RED_STAINED_GLASS_PANE)

        val displayName = if (gui.hasNextPage()) {
            "&ePrevious page &8[&b${gui.currentPage - 1}&7/&c${gui.pageAmount}&8]".colorized()
        } else {
            "&ePrevious page &8[&b${gui.currentPage}&7/&c${gui.pageAmount}&8]".colorized()
        }

        builder.setDisplayName(displayName)

        if (!gui.hasPreviousPage()) {
            builder.addLoreLines("", "&cThere are no more pages".colorized())
        }

        return builder
    }

}