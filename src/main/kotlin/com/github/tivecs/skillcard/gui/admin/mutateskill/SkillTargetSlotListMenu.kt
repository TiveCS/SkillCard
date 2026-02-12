package com.github.tivecs.skillcard.gui.admin.mutateskill

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.skill.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenMutateSkillMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenMutateSkillTargetSlotMenuItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window

object SkillTargetSlotListMenu {

    fun open(viewer: Player, skillBuilder: SkillBuilder, searchText: String = "") {
        var search = searchText

        val items = skillBuilder.targetSlots.map {

        }

        val gui = PagedGui.items()
            .setStructure(
                "#########",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "#B##S##C#"
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient(
                'S', OpenInputStringMenuItem(
                    displayName = "&aSearch Target Slot",
                    material = XMaterial.COMPASS.get() ?: Material.COMPASS,
                    title = "Search Target Slot",
                    initialValue = search,
                    onRename = { input -> search = input },
                    onConfirm = { _, _, _ -> open(viewer, skillBuilder, search) },
                )
            )
            .addIngredient(
                'B', OpenMutateSkillMenuItem(
                    displayText = "&eBack to Mutate Skill Menu",
                )
            )
            .addIngredient(
                'C', OpenMutateSkillTargetSlotMenuItem(
                    builder = skillBuilder
                )
            )
            .build()

        val window = Window.single().setGui(gui).setViewer(viewer).build()

        window.open()
    }

}