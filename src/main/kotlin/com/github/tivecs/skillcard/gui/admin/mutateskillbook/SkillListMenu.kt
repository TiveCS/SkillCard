package com.github.tivecs.skillcard.gui.admin.mutateskillbook

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.entities.skills.Skill
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenMutateSkillMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.RegisteredSkillItem
import com.github.tivecs.skillcard.gui.common.items.BorderGuiItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import com.github.tivecs.skillcard.internal.data.repositories.SkillRepository
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.window.Window

object SkillListMenu {

    fun open(
        player: Player,
        backItem: Item = BorderGuiItem,
        onSkillItemClick: ((player: Player, skill: Skill) -> Unit) = { _, _ -> },
        searchText: String = "",
        enableCreate: Boolean = true
    ) {
        var searchTextValue = searchText

        val registeredSkills = SkillRepository.cached.values.map {
            RegisteredSkillItem(
                skill = it,
                displayMaterial = it.material.get() ?: Material.BLAZE_POWDER,
                onClick = onSkillItemClick,
            )
        }

        val gui = PagedGui.items()
            .setStructure(
                "#########",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "#B##S##C#"
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient('B', backItem)
            .addIngredient(
                'S', OpenInputStringMenuItem(
                    displayName = "&6Search Skills",
                    material = XMaterial.COMPASS.get() ?: Material.COMPASS,
                    title = "Search Skill",
                    initialValue = searchTextValue,
                    onRename = { input -> searchTextValue = input },
                    onConfirm = { _, _, _ ->
                        open(player, backItem, onSkillItemClick, searchTextValue)
                    },
                    lores = listOf(" ", "&fCurrent Keyword: $searchTextValue".colorized())
                )
            )
            .addIngredient(
                'C',
                if (enableCreate)
                    OpenMutateSkillMenuItem()
                else
                    BorderGuiItem
            )
            .setContent(registeredSkills)
            .build()

        val window = Window.single()
            .setTitle("Skills List")
            .setGui(gui)
            .setViewer(player)
            .build()

        window.open()
    }

}