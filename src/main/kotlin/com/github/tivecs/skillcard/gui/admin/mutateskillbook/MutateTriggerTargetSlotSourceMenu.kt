package com.github.tivecs.skillcard.gui.admin.mutateskillbook

import com.github.tivecs.skillcard.core.builders.book.TriggerSkillSetBuilder
import com.github.tivecs.skillcard.core.entities.skills.SkillTargetSlot
import com.github.tivecs.skillcard.gui.admin.common.SelectionItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.ConfirmMutateTriggerTargetSlotSourceMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenMutateSkillBookSkillSetMenuItem
import com.github.tivecs.skillcard.gui.common.items.BorderGuiItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.window.Window

object MutateTriggerTargetSlotSourceMenu {

    fun open(
        player: Player,
        skillSetBuilder: TriggerSkillSetBuilder,
        backItem: Item = BorderGuiItem,
        searchText: String = "",
        targetSlotPredicate: (SkillTargetSlot) -> Boolean = { true },
    ) {
        var searchTextValue = searchText

        val availableTargetSlots = skillSetBuilder.skills
            .flatMap { it.targetSlots }
            .map {
                SelectionItem(
                    value = it,
                    displayName = { targetSlot -> "&6${targetSlot.identifier}" },
                    displayLores = { targetSlot ->
                        listOf(
                            " ",
                            "&aData Type: &f${targetSlot.targetType?.simpleName}",
                            " ",
                            "&fClick to edit this target slot's source")
                    },
                    onClick = { player, targetSlot ->
                        SetTriggerTargetSlotSourceTargetMenu.open(
                            player = player,
                            skillSetBuilder = skillSetBuilder,
                            targetSlot = targetSlot,
                            onPostSet = { _ ->
                                open(
                                    player,
                                    skillSetBuilder,
                                    backItem,
                                    searchTextValue,
                                    targetSlotPredicate
                                )
                            }
                        )
                    }
                )
            }

        val gui = PagedGui.items()
            .setStructure(
                "####S####",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "####B####",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient('B', backItem)
            .addIngredient(
                'S', OpenInputStringMenuItem(
                    displayName = "&6Search Registered Target Slots",
                    title = "Search Registered Target Slot",
                    material = Material.COMPASS,
                    initialValue = searchText,
                    onRename = { input -> searchTextValue = input },
                    onConfirm = { _, _, _ ->
                        open(player, skillSetBuilder, backItem, searchTextValue, targetSlotPredicate)
                    },
                )
            )
            .setContent(availableTargetSlots)
            .build()

        val window = Window.single()
            .setTitle("Target Slot Source Editor")
            .setGui(gui)
            .setViewer(player)
            .build()

        window.open()
    }

}