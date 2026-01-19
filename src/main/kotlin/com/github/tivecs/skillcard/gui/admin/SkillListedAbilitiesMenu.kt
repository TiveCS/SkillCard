package com.github.tivecs.skillcard.gui.admin

import com.github.tivecs.skillcard.core.skills.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.items.OpenAddAbilityToSkillMenuItem
import com.github.tivecs.skillcard.gui.admin.items.OpenConfigureSkillAbilityMenuItem
import com.github.tivecs.skillcard.gui.admin.items.OpenSkillCreateMenuItem
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window

object SkillListedAbilitiesMenu {

    fun open(viewer: Player, builder: SkillBuilder) {
        val skillAbilityItems = builder.abilities.map {
            OpenConfigureSkillAbilityMenuItem(it, builder)
        }

        val gui = PagedGui.items()
            .setStructure(
                "####B####",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "###.1.##S",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient('B', OpenSkillCreateMenuItem("&a&lBack to Skill Creation"))
            .addIngredient('1', OpenAddAbilityToSkillMenuItem(builder))
            .build()

        val window = Window.single()
            .setGui(gui)
            .setTitle("Listed Abilities")
            .setViewer(viewer)
            .build()

        window.open()
    }

}