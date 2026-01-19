package com.github.tivecs.skillcard.gui.admin

import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.skills.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.items.AbilitySelectItem
import com.github.tivecs.skillcard.gui.admin.items.OpenSkillListedAbilitiesMenuItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import com.github.tivecs.skillcard.internal.data.repositories.AbilityRepository
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window
import java.util.function.Consumer

object SkillAbilitySelectionMenu {

    fun open(viewer: Player, builder: SkillBuilder, onSelect: Consumer<Ability<*>>, searchText: String = "") {
        var search = searchText.trim()

        val abilityItems = AbilityRepository.getAllAbilities().filter {
            if (search.isBlank()) true
            else
                it.displayName.contains(search, true)
        }.map {
            AbilitySelectItem(it, { ability ->
                onSelect.accept(ability)
            })
        }

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
                'S', OpenInputStringMenuItem(
                    displayName = "&e&lSearch Ability",
                    title = "Search Ability",
                    initialValue = search,
                    onRename = { text ->
                        search = text.trim()
                    },
                    onConfirm = { _, _, _ ->
                        open(viewer, builder, onSelect, search)
                    },
                )
            )
            .addIngredient('B', OpenSkillListedAbilitiesMenuItem(builder, "&a&lBack to Skill Ability List"))
            .setContent(abilityItems)
            .build()

        val window = Window.single()
            .setGui(gui)
            .setTitle("Skill Ability Selection Menu")
            .setViewer(viewer)
            .build()

        window.open()
    }

}