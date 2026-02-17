package com.github.tivecs.skillcard.gui.admin.mutateskillbook

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.book.SkillBookBuilder
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.ConfirmMutateSkillBookSkillSetItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenMutateSkillBookMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenMutateSkillBookSkillSetMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenMutateTriggerTargetSlotSourceMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenSkillListMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenTriggerListMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenTriggerTargetSlotSourceListMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.RegisteredSkillItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import com.github.tivecs.skillcard.internal.data.repositories.SkillRepository
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window

object MutateSkillBookSkillSetMenu {

    fun open(player: Player, skillBookBuilder: SkillBookBuilder, searchText: String = "") {
        var searchTextValue = searchText
        val skillSetBuilder =
            skillBookBuilder.triggerSkillSetBuilder ?: skillBookBuilder.newTriggerSkillSetBuilder();

        val registeredSkills = skillSetBuilder.skills.map {
            RegisteredSkillItem(
                skill = it,
                displayLores = { _ ->
                    listOf(" ", "&cClick here to remove this skill from this skill set".colorized())
                },
                onClick = { player, skill ->
                    skillSetBuilder.skills.remove(skill)
                    open(player, skillBookBuilder, searchTextValue)
                }
            )
        }

        val gui = PagedGui.items()
            .setStructure(
                "####B####",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "##S###C##",
                "#1234567#"
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient(
                'B', OpenMutateSkillBookMenuItem(
                    displayText = "&eBack to Skill Book Menu",
                )
            )
            .addIngredient(
                'S', OpenInputStringMenuItem(
                    displayName = "&6Search Registered Skills",
                    material = XMaterial.COMPASS.get() ?: Material.COMPASS,
                    title = "Search Registered Skill",
                    initialValue = searchTextValue,
                    onRename = { input -> searchTextValue = input },
                    onConfirm = { _, _, _ ->
                        open(player, skillBookBuilder, searchTextValue)
                    },
                    lores = listOf(" ", "&fCurrent Keyword: $searchTextValue".colorized())
                )
            )
            .addIngredient(
                'C', ConfirmMutateSkillBookSkillSetItem(
                    skillSetBuilder = skillSetBuilder,
                    onPostConfirm = { player, _, _ ->
                        SkillBookSkillSetListMenu.open(player, skillBookBuilder)
                    }
                )
            )
            .addIngredient(
                '1', OpenTriggerListMenuItem(
                    onMenuItemClick = { player, trigger ->
                        skillSetBuilder.setTrigger(trigger)
                        open(player, skillBookBuilder, searchTextValue)
                    },
                    lores = listOf(
                        " ",
                        "&fClick here to select this skill set trigger",
                        " ",
                        "&fCurrent Trigger: &b${skillSetBuilder.trigger?.identifier ?: "&cNONE"}".colorized()
                    ),
                    backItem = OpenMutateSkillBookSkillSetMenuItem(
                        displayText = "&eBack to Skill Set Editor",
                        bookBuilder = skillBookBuilder,
                    ),
                )
            )
            .addIngredient(
                '2', OpenSkillListMenuItem(
                    displayText = "&eAdd Skill to Skill Set",
                    lores = listOf(" ", "&fRegistered &b${skillSetBuilder.skills.size}&f skills on this skill set"),
                    backItem = OpenMutateSkillBookSkillSetMenuItem(
                        bookBuilder = skillBookBuilder,
                        displayText = "&6Back to Skill Set Editor",
                    ),
                    onSkillItemClick = { player, skill ->
                        skillSetBuilder.registerSkills(skill)
                        open(player, skillBookBuilder, searchTextValue)
                    }
                )
            )
            .addIngredient(
                '3', OpenTriggerTargetSlotSourceListMenuItem(
                    displayText = "&eAdd Target Slot Source",
                    lores = listOf(
                        " ",
                        "&fRegistered &b${skillSetBuilder.targetSlotSources.size}&f target slot sources on this skill set"
                    ),
                    skillSetBuilder = skillSetBuilder,
                )
            )
            .setContent(registeredSkills)
            .build()

        val window = Window.single()
            .setTitle("Skill Set Editor")
            .setGui(gui)
            .setViewer(player)
            .build()

        window.open()
    }

}