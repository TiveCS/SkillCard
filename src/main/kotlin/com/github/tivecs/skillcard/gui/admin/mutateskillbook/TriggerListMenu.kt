package com.github.tivecs.skillcard.gui.admin.mutateskillbook

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.entities.triggers.Trigger
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.RegisteredTriggerItem
import com.github.tivecs.skillcard.gui.common.items.BorderGuiItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import com.github.tivecs.skillcard.internal.data.repositories.TriggerRepository
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.window.Window

object TriggerListMenu {

    fun open(
        player: Player,
        backItem: Item = BorderGuiItem,
        onClick: ((player: Player, trigger: Trigger<*>) -> Unit) = { _, _ -> },
        searchText: String = "",
        predicate: (Trigger<*>) -> Boolean = { true },
        triggerItemLores: (trigger: Trigger<*>) -> List<String> = { emptyList() },
        displayName: (trigger: Trigger<*>) -> String = { it.identifier },
    ) {
        var searchTextValue = searchText

        val registeredTriggers = TriggerRepository.registeredTriggers.values.map {
            RegisteredTriggerItem(
                trigger = it,
                onClick = onClick,
                displayName = displayName,
                displayLores = triggerItemLores
            )
        }

        val gui = PagedGui.items()
            .setStructure(
                "#########",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "##B###S##"
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient('B', backItem)
            .addIngredient(
                'S', OpenInputStringMenuItem(
                    displayName = "&aSearch Trigger",
                    material = XMaterial.COMPASS.get() ?: Material.COMPASS,
                    title = "Search Trigger",
                    initialValue = searchTextValue,
                    onRename = { input -> searchTextValue = input },
                    onConfirm = { _, _, _ ->
                        open(player, backItem, onClick, searchTextValue, predicate, triggerItemLores, displayName)
                    },
                    lores = listOf(
                        " ", "&fCurrent Keyword: ${searchTextValue}".colorized()
                    )
                )
            )
            .setContent(registeredTriggers)
            .build()

        val window = Window.single()
            .setTitle("Triggers List")
            .setGui(gui)
            .setViewer(player)
            .build()

        window.open()
    }

}