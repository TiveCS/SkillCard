package com.github.tivecs.skillcard.gui.admin

import com.github.tivecs.skillcard.core.skills.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.items.OpenSkillCreateMenuItem
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window

object SkillTriggerTargetSlotMenu {

    fun open(viewer: Player, builder: SkillBuilder) {
        val gui = PagedGui.items()
            .setStructure(
                "#########",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "####B####",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient('B', OpenSkillCreateMenuItem(displayText = "&a&lBack to Skill Creation Hub"))
            .build()

        val window = Window.single()
            .setGui(gui)
            .setViewer(viewer)
            .build()

        window.open()
    }

}