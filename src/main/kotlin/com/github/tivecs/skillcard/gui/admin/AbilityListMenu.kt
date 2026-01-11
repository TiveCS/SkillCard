package com.github.tivecs.skillcard.gui.admin

import com.github.tivecs.skillcard.gui.admin.items.OpenAdminManageMenuItem
import com.github.tivecs.skillcard.gui.admin.items.OpenSkillBookListMenuItem
import com.github.tivecs.skillcard.gui.admin.items.OpenSkillListMenuItem
import com.github.tivecs.skillcard.internal.data.repositories.AbilityRepository
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.Window

object AbilityListMenu {
    fun open(viewer: Player) {
        val abilityItems = mutableListOf<SimpleItem>()

        AbilityRepository.getAllAbilitiesWithSources().forEach { sourcePlugin, abilities ->
            abilityItems.addAll(abilities.map { ability ->
                SimpleItem(ItemBuilder(ability.displayItem()))
            })
        }

        val gui = PagedGui.items()
            .setStructure(
                "###ABC###",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "#########",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_VERTICAL)
            .addIngredient('A', OpenSkillListMenuItem())
            .addIngredient('B', OpenAdminManageMenuItem())
            .addIngredient('C', OpenSkillBookListMenuItem())
            .setContent(abilityItems.toList())
            .build()

        val window = Window.single()
            .setGui(gui)
            .setViewer(viewer)
            .build()

        window.open()
    }
}