package com.github.tivecs.skillcard.gui.admin

import com.github.tivecs.skillcard.gui.admin.items.OpenAbilityListMenuItem
import com.github.tivecs.skillcard.gui.admin.items.OpenSkillBookListMenuItem
import com.github.tivecs.skillcard.gui.admin.items.OpenSkillListMenuItem
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.window.Window

object SkillAdminManageMenu {
    fun open(viewer: Player) {
        val lobbyMenu = Gui.normal()
            .setStructure(
                "###ABC###",
            )
            .addIngredient('A', OpenSkillBookListMenuItem())
            .addIngredient('B', OpenSkillListMenuItem())
            .addIngredient('C', OpenAbilityListMenuItem())
            .build()

        val window = Window
            .single()
            .setGui(lobbyMenu)
            .setViewer(viewer)
            .build()

        window.open()
    }
}