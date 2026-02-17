package com.github.tivecs.skillcard.gui.admin.mutateskillbook

import com.github.tivecs.skillcard.core.builders.book.TriggerSkillSetBuilder
import com.github.tivecs.skillcard.core.builders.book.TriggerTargetSlotSourceBuilder
import com.github.tivecs.skillcard.core.entities.books.TriggerTargetSlotSource
import com.github.tivecs.skillcard.core.entities.skills.SkillTargetSlot
import com.github.tivecs.skillcard.core.entities.triggers.AvailableTriggerTarget
import com.github.tivecs.skillcard.gui.admin.common.SelectionItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenMutateTriggerTargetSlotSourceMenuItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window

object SetTriggerTargetSlotSourceTargetMenu {

    fun open(
        player: Player,
        skillSetBuilder: TriggerSkillSetBuilder,
        targetSlot: SkillTargetSlot,
        onPostSet: (TriggerTargetSlotSource) -> Unit = { _ -> },
    ) {
        val availableTargets = skillSetBuilder.trigger?.availableTargets?.map { target ->
            SelectionItem(
                value = target,
                displayName = { target -> "&6${target.key}" },
                displayLores = { target -> listOf(" ", "&aData Type: &b${target.outputType.simpleName}")},
                material = { Material.BLACK_BANNER },
                onClick = { _, target ->
                    val targetSourceBuilder = skillSetBuilder.newTriggerTargetSlotSourceBuilder()

                    targetSourceBuilder.setTarget(target)
                    targetSourceBuilder.setSkillTargetSlot(targetSlot)

                    val targetSlotSource = targetSourceBuilder.build()
                    skillSetBuilder.targetSlotSourceBuilder = null
                    skillSetBuilder.targetSlotSources.add(targetSlotSource)

                    onPostSet(targetSlotSource)
                }
            )
        } ?: emptyList()

        val gui = PagedGui.items()
            .setStructure(
                "####B####",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "####S####",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient(
                'B', OpenMutateTriggerTargetSlotSourceMenuItem(
                    displayText = "&6Back to Target Slot Source Editor",
                    skillSetBuilder = skillSetBuilder
                )
            )
            .setContent(availableTargets)
            .build()

        val window = Window.single()
            .setViewer(player)
            .setTitle("Set Target Slot Source - ${targetSlot.identifier}")
            .setGui(gui)
            .build()

        window.open()
    }

}