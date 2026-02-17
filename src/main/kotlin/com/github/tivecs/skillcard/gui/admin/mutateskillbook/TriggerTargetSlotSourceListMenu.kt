package com.github.tivecs.skillcard.gui.admin.mutateskillbook

import com.github.tivecs.skillcard.core.builders.book.SkillBookBuilder
import com.github.tivecs.skillcard.core.builders.book.TriggerSkillSetBuilder
import com.github.tivecs.skillcard.core.entities.books.TriggerTargetSlotSource
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenMutateTriggerTargetSlotSourceMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenTriggerTargetSlotSourceListMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.RegisteredTriggerTargetSlotSourceItem
import com.github.tivecs.skillcard.gui.common.items.BorderGuiItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.window.Window

object TriggerTargetSlotSourceListMenu {

    fun open(
        player: Player,
        skillSetBuilder: TriggerSkillSetBuilder,
        searchText: String = "",
        onTargetSlotSourceClick: (Player, TriggerTargetSlotSource) -> Unit = { _, _ -> },
        predicate: (TriggerTargetSlotSource) -> Boolean = { true },
        backItem: Item = BorderGuiItem
    ) {
        var searchTextValue = searchText

        val registeredSources = skillSetBuilder.targetSlotSources.map {
            RegisteredTriggerTargetSlotSourceItem(
                displayLores = { _ ->
                    listOf(" ", "&cClick here to edit this target slot source".colorized())
                },
                onClick = onTargetSlotSourceClick,
                triggerTargetSlotSource = it,
            )
        }

        val gui = PagedGui.items()
            .setStructure(
                "#########",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "##B#S#C##",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient('B', backItem)
            .addIngredient(
                'C', OpenMutateTriggerTargetSlotSourceMenuItem(
                    skillSetBuilder = skillSetBuilder,
                )
            )
            .addIngredient(
                'S', OpenInputStringMenuItem(
                    displayName = "&aSearch Trigger Target Slot Source",
                    title = "Search Trigger Target Slot Source",
                    initialValue = searchText,
                    onRename = { input ->
                        searchTextValue = input
                    },
                    onConfirm = { _, _, _ ->
                        open(
                            player,
                            skillSetBuilder,
                            searchTextValue,
                            onTargetSlotSourceClick,
                            predicate,
                            backItem
                        )
                    },
                )
            )
            .setContent(registeredSources)
            .build()

        val window = Window.single()
            .setTitle("Target Slot Sources List")
            .setGui(gui)
            .setViewer(player)
            .build()

        window.open()
    }

}