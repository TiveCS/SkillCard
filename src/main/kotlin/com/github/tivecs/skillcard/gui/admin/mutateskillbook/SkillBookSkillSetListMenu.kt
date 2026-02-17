package com.github.tivecs.skillcard.gui.admin.mutateskillbook

import com.github.tivecs.skillcard.core.builders.book.SkillBookBuilder
import com.github.tivecs.skillcard.core.entities.books.TriggerSkillSet
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenMutateSkillBookMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenMutateSkillBookSkillSetMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.RegisteredSkillSetItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window

object SkillBookSkillSetListMenu {

    fun open(
        player: Player,
        skillBookBuilder: SkillBookBuilder,
        searchText: String = "",
        predicate: (TriggerSkillSet) -> Boolean = { true },
        onSkillSetClick: (Player, TriggerSkillSet) -> Unit = { _, _ -> }
    ) {
        var searchTextValue = searchText

        val registeredSkillSets = skillBookBuilder.triggerSkillSets.map {
            RegisteredSkillSetItem(
                skillSet = it,
                displayLores = { _ ->
                    listOf(" ", "&cClick here to edit this skill set".colorized())
                },
                onClick = onSkillSetClick,
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
            .addIngredient(
                'B', OpenMutateSkillBookMenuItem(
                    displayText = "&eBack to Skill Book Menu",
                )
            )
            .addIngredient(
                'S', OpenInputStringMenuItem(
                    displayName = "&6Search Registered Skill Sets",
                    title = "Search Registered Skill Set",
                    initialValue = searchTextValue,
                    onRename = { input -> searchTextValue = input },
                    onConfirm = { _, _, _ ->
                        open(player, skillBookBuilder, searchTextValue, predicate)
                    },
                )
            )
            .addIngredient(
                'C', OpenMutateSkillBookSkillSetMenuItem(
                    displayText = "&aCreate New Skill Set",
                    bookBuilder = skillBookBuilder,
                    lores = listOf(" ", "&fRegistered &b${skillBookBuilder.triggerSkillSets.size}&f skill sets".colorized()),
                )
            )
            .setContent(registeredSkillSets)
            .build()

        val window = Window.single()
            .setTitle("Skill Sets List")
            .setViewer(player)
            .setGui(gui)
            .build()

        window.open()
    }

}