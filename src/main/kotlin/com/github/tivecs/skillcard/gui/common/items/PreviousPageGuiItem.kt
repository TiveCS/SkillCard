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
        builder.setDisplayName("&ePrevious page".colorized())
            .addLoreLines(
                if (gui.hasNextPage())
                    ("&3Go to page &b" + (gui.currentPage + 2) + "&7/&c" + gui.pageAmount).colorized()
                else "&cThere are no more pages".colorized()
            )
        return builder
    }

}