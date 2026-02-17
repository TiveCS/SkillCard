package com.github.tivecs.skillcard.gui.admin.lobby

import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenMutateSkillMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenMutateSkillBookMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenSkillListMenuItem
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.window.Window

object AdminLobbyMenu {

    fun open(player: Player) {
        val gui = Gui.normal()
            .setStructure(
                "#########",
                "#1a###3b#",
                "#########",
            )
            .addIngredient('1', OpenMutateSkillBookMenuItem())
            .addIngredient('3', OpenMutateSkillMenuItem())
            .addIngredient('b', OpenSkillListMenuItem())
            .build()

        val window = Window.single().setGui(gui).setViewer(player).build()

        window.open()
    }

}