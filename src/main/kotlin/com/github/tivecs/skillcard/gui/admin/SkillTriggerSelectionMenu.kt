package com.github.tivecs.skillcard.gui.admin

import com.github.tivecs.skillcard.core.skills.SkillBuilder
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui

object SkillTriggerSelectionMenu {

    fun open(viewer: Player, builder: SkillBuilder) {
        val gui = PagedGui.items()
            .setStructure(
                "#########",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "####B####"
            )
    }

}